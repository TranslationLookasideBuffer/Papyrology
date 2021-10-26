grammar Papyrus
    ;

script: line_end* header script_line* EOF;

header: K_SCRIPT_NAME ID (K_EXTENDS ID)? flag = (F_HIDDEN | F_CONDITIONAL)* line_end_doc_comment;

script_line:          import_declaration | variable_declaration | state_declaration | property_declaration | function_declaration | event_declaration | line_end;
import_declaration:   K_IMPORT ID line_end;
variable_declaration: type ID (O_ASSIGN value = literal)? F_CONDITIONAL* line_end;
state_declaration:    K_AUTO? K_STATE ID line_end (function_declaration | event_declaration | line_end)* K_END_STATE line_end;
event_declaration:    K_EVENT ID S_LPAREN parameters S_RPAREN K_NATIVE? line_end_doc_comment (statement_block K_END_EVENT line_end)?;
property_declaration
    : type K_PROPERTY ID F_HIDDEN? line_end_doc_comment line_end* property_function line_end* property_function? line_end* K_END_PROPERTY line_end # Full
    | type K_PROPERTY ID (O_ASSIGN value = literal)? K_AUTO (F_HIDDEN | F_CONDITIONAL)* line_end_doc_comment                                       # Auto
    | type K_PROPERTY ID O_ASSIGN value = literal K_AUTO_READ_ONLY F_HIDDEN? line_end_doc_comment                                                  # AutoReadOnly
    | type K_PROPERTY ID O_ASSIGN value = literal (K_AUTO | K_AUTO_READ_ONLY) F_CONDITIONAL line_end_doc_comment                                   # Conditional
    ;
property_function
    : type K_FUNCTION ID S_LPAREN S_RPAREN line_end statement_block K_END_FUNCTION line_end      # Get
    | K_FUNCTION ID S_LPAREN parameter S_RPAREN line_end statement_block K_END_FUNCTION line_end # Set
    ;
function_declaration
    : type? K_FUNCTION ID S_LPAREN parameters S_RPAREN flag += K_GLOBAL? line_end_doc_comment statement_block K_END_FUNCTION line_end
    | type? K_FUNCTION ID S_LPAREN parameters S_RPAREN flag += K_GLOBAL? flag += K_NATIVE flag += K_GLOBAL? line_end_doc_comment
    ;

statement_block: statement*;
statement
    : type ID (O_ASSIGN value = expression)? line_end                                                                                                       # Define
    | statement_assign_value op = (O_ASSIGN | O_ASSIGN_ADD | O_ASSIGN_SUBTRACT | O_ASSIGN_MULTIPLY | O_ASSIGN_DIVIDE | O_ASSIGN_MODULO) expression line_end # Assign
    | K_RETURN expression? line_end                                                                                                                         # Return
    | K_IF expression line_end statement_block (K_ELSE_IF expression line_end statement_block)* (K_ELSE line_end statement_block)? K_END_IF line_end        # If
    | K_WHILE expression line_end statement_block K_END_WHILE line_end                                                                                      # While
    | expression line_end                                                                                                                                   # StandaloneExpression
    | line_end                                                                                                                                              # BlankLine
    ;
statement_assign_value: ID | expression O_DOT ID | expression S_LBRAKET expression S_RBRAKET;
expression
    : a = expression op = O_LOGICAL_OR b = expression                                                                        # BinaryOp
    | a = expression op = O_LOGICAL_AND b = expression                                                                       # BinaryOp
    | a = expression op = (O_EQUAL | O_NOT_EQUAL | O_GREATER | O_GREATER_OR_EQUAL | O_LESS | O_LESS_OR_EQUAL) b = expression # BinaryOp
    | a = expression op = (O_ADD | O_SUBTRACT) b = expression                                                                # BinaryOp
    | a = expression op = (O_MULTIPLY | O_DIVIDE | O_MODULO) b = expression                                                  # BinaryOp
    | op = (O_SUBTRACT | O_LOGICAL_NOT) value = expression                                                                   # UnaryOp
    | expression K_AS type                                                                                                   # Cast
    | ID S_LPAREN call_parameters S_RPAREN                                                                                   # LocalFunctionCall
    | expression O_DOT (K_LENGTH | ID) (S_LPAREN call_parameters S_RPAREN)?                                                  # DotOrFunctionCall
    | S_LPAREN expression S_RPAREN                                                                                           # Parenthetical
    | expression S_LBRAKET index = expression S_RBRAKET                                                                      # ArrayAccess
    | K_NEW type S_LBRAKET size = L_UINT S_RBRAKET                                                                           # ArrayInitialize
    | literal                                                                                                                # LiteralValue
    | ID                                                                                                                     # ID
    ;
call_parameters: params += call_parameter? (S_COMMA params += call_parameter)*;
call_parameter:  (ID O_ASSIGN)? expression;

type:                 (K_INT | K_BOOL | K_FLOAT | K_STRING | ID) (S_LBRAKET S_RBRAKET)?;
literal:              K_TRUE | K_FALSE | L_FLOAT | L_UINT | L_INT | L_STRING | K_NONE | K_SELF | K_PARENT;
parameters:           params += parameter? (S_COMMA params += parameter)*;
parameter:            type ID (O_ASSIGN value = literal)?;
line_end:             LINE_COMMENT? NEWLINE;
line_end_doc_comment: (LINE_COMMENT? NEWLINE)+ (DOC_COMMENT LINE_COMMENT? NEWLINE)?;

// Handle Case-Insensitivity
fragment A: [aA];
fragment B: [bB];
fragment C: [cC];
fragment D: [dD];
fragment E: [eE];
fragment F: [fF];
fragment G: [gG];
fragment H: [hH];
fragment I: [iI];
fragment J: [jJ];
fragment K: [kK];
fragment L: [lL];
fragment M: [mM];
fragment N: [nN];
fragment O: [oO];
fragment P: [pP];
fragment Q: [qQ];
fragment R: [rR];
fragment S: [sS];
fragment T: [tT];
fragment U: [uU];
fragment V: [vV];
fragment W: [wW];
fragment X: [xX];
fragment Y: [yY];
fragment Z: [zZ];

fragment DIGIT:         [0-9];
fragment HEX_DIGIT:     DIGIT | A | B | C | D | E | F;
fragment STRING_ESCAPE: '\\n' | '\\t' | '\\\\' | '\\"';

// Symbols
S_LPAREN:  '(';
S_RPAREN:  ')';
S_LBRAKET: '[';
S_RBRAKET: ']';
S_LCURLY:  '{';
S_RCURLY:  '}';
S_COMMA:   ',';

// Keywords
K_AS:             A S;
K_AUTO:           A U T O;
K_AUTO_READ_ONLY: A U T O R E A D O N L Y;
K_BOOL:           B O O L;
K_ELSE:           E L S E;
K_ELSE_IF:        E L S E I F;
K_END_EVENT:      E N D E V E N T;
K_END_FUNCTION:   E N D F U N C T I O N;
K_END_IF:         E N D I F;
K_END_PROPERTY:   E N D P R O P E R T Y;
K_END_STATE:      E N D S T A T E;
K_END_WHILE:      E N D W H I L E;
K_EVENT:          E V E N T;
K_EXTENDS:        E X T E N D S;
K_FALSE:          F A L S E;
K_FLOAT:          F L O A T;
K_FUNCTION:       F U N C T I O N;
K_GLOBAL:         G L O B A L;
K_IF:             I F;
K_IMPORT:         I M P O R T;
K_INT:            I N T;
K_LENGTH:         L E N G T H;
K_NATIVE:         N A T I V E;
K_NEW:            N E W;
K_NONE:           N O N E;
K_PARENT:         P A R E N T;
K_PROPERTY:       P R O P E R T Y;
K_RETURN:         R E T U R N;
K_SCRIPT_NAME:    S C R I P T N A M E;
K_SELF:           S E L F;
K_STATE:          S T A T E;
K_STRING:         S T R I N G;
K_TRUE:           T R U E;
K_WHILE:          W H I L E;

// Literals
L_UINT:   DIGIT+ | '0' X HEX_DIGIT+;
L_INT:    O_SUBTRACT DIGIT+ | L_UINT;
L_FLOAT:  O_SUBTRACT? DIGIT+ O_DOT DIGIT+;
L_STRING: '"' (STRING_ESCAPE | .)*? '"';

// Flags (that are not also keywords)
F_HIDDEN:      H I D D E N;
F_CONDITIONAL: C O N D I T I O N A L;

// Operators
O_DOT:              '.';
O_ASSIGN:           '=';
O_ASSIGN_ADD:       '+=';
O_ASSIGN_SUBTRACT:  '-=';
O_ASSIGN_MULTIPLY:  '*=';
O_ASSIGN_DIVIDE:    '/=';
O_ASSIGN_MODULO:    '%=';
O_LOGICAL_NOT:      '!';
O_LOGICAL_OR:       '||';
O_LOGICAL_AND:      '&&';
O_MULTIPLY:         '*';
O_DIVIDE:           '/';
O_MODULO:           '%';
O_ADD:              '+';
O_SUBTRACT:         '-';
O_EQUAL:            '==';
O_NOT_EQUAL:        '!=';
O_GREATER:          '>';
O_GREATER_OR_EQUAL: '>=';
O_LESS:             '<';
O_LESS_OR_EQUAL:    '<=';

NEWLINE:       '\r'? '\n';
LINE_BREAK:    '\\' [ \t]* '\r'? '\n' -> skip; // Lines that end in "\" continue onto the next line.
WS:            [ \t]+ -> skip;
LINE_COMMENT:  ';' ~[\n]* -> skip;
BLOCK_COMMENT: ';/' .*? '/;' -> skip;
DOC_COMMENT:   S_LCURLY .*? S_RCURLY;

ID: [a-zA-Z_] ([a-zA-Z_] | DIGIT)*;