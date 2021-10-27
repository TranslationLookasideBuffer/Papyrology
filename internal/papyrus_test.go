package papyrus

import (
	"io/fs"
	"io/ioutil"
	"path/filepath"
	"testing"

	"papyrus/parser"

	"github.com/antlr/antlr4/runtime/Go/antlr"
)

func TestGrammar(t *testing.T) {
	for path, content := range getScripts(t) {
		t.Run(path, func(t *testing.T) {
			fis := antlr.NewInputStream(content)
			lexer := parser.NewPapyrusLexer(fis)
			stream := antlr.NewCommonTokenStream(lexer, antlr.TokenDefaultChannel)
			p := parser.NewPapyrusParser(stream)
			p.AddErrorListener(&errorListener{t: t})

			antlr.ParseTreeWalkerDefault.Walk(&listener{}, p.Script())
		})
	}
}

type listener struct {
	*parser.BasePapyrusListener
}

type errorListener struct {
	*antlr.DefaultErrorListener
	t *testing.T
}

func (el *errorListener) SyntaxError(recognizer antlr.Recognizer, offendingSymbol interface{}, line, column int, msg string, e antlr.RecognitionException) {
	el.t.Fatalf("syntax error on line %d at char %d: %s", line, column, msg)
}

func getScripts(t *testing.T) map[string]string {
	t.Helper()
	scripts := make(map[string]string)
	err := filepath.WalkDir("../test/scripts", func(path string, d fs.DirEntry, err error) error {
		if err != nil {
			return err
		}
		if d.IsDir() {
			return nil
		}
		bytes, err := ioutil.ReadFile(path)
		if err != nil {
			return err
		}
		scripts[path] = string(bytes) + "\n"
		return nil
	})
	if err != nil {
		t.Fatalf("failed to read scripts: %v", err)
	}
	return scripts
}
