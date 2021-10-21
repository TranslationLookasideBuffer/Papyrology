grammar Papyrus
    ;

script: LINE_END* header script_line*;

header: K_SCRIPT_NAME ID (K_EXTENDS ID)? flag = (F_HIDDEN | F_CONDITIONAL)* LINE_END_DOC_COMMENT;

script_line:          import_declaration | variable_declaration | state_declaration | property_declaration | function_declaration | event_declaration | LINE_END;
import_declaration:   K_IMPORT ID LINE_END;
variable_declaration: type ID (O_ASSIGN value = literal) F_CONDITIONAL* LINE_END;
state_declaration:    K_AUTO? K_STATE ID LINE_END (function_declaration | event_declaration)* K_END_STATE LINE_END;
event_declaration:    K_EVENT ID '(' parameters ')' K_NATIVE? LINE_END_DOC_COMMENT (statement_block K_END_EVENT LINE_END)?;
property_declaration
    : type K_PROPERTY ID F_HIDDEN? LINE_END property_function property_function? K_END_PROPERTY LINE_END_DOC_COMMENT # Full
    | type K_PROPERTY ID (O_ASSIGN value = literal)? K_AUTO F_HIDDEN? LINE_END_DOC_COMMENT                           # Auto
    | type K_PROPERTY ID O_ASSIGN value = literal K_AUTO_READ_ONLY F_HIDDEN? LINE_END_DOC_COMMENT                    # AutoReadOnly
    | type K_PROPERTY ID O_ASSIGN value = literal (K_AUTO | K_AUTO_READ_ONLY) F_CONDITIONAL LINE_END_DOC_COMMENT     # Conditional
    ;
property_function
    : type K_FUNCTION I_GET '(' ')' LINE_END statement_block K_END_FUNCTION LINE_END      # Get
    | K_FUNCTION I_SET '(' parameter ')' LINE_END statement_block K_END_FUNCTION LINE_END # Set
    ;
function_declaration: type? K_FUNCTION ID '(' parameters ')' flag += (K_NATIVE | K_GLOBAL)* LINE_END_DOC_COMMENT (statement_block K_END_FUNCTION LINE_END)?;

statement_block: statement*;
statement
    : type ID (O_ASSIGN value = expression)? LINE_END                                                                                                # Define
    | statement_assign_value op = O_ASSIGNMENT expression LINE_END                                                                                   # Assign
    | K_RETURN expression? LINE_END                                                                                                                  # Return
    | K_IF expression LINE_END statement_block (K_ELSE_IF expression LINE_END statement_block)* (K_ELSE LINE_END statement_block)? K_END_IF LINE_END # If
    | K_WHILE expression LINE_END statement_block K_END_WHILE LINE_END                                                                               # While
    | LINE_END                                                                                                                                       # BlankLine
    ;
statement_assign_value: ID | expression O_DOT ID | expression '[' expression ']';
expression
    : ID                                                          # ID
    | K_NEW type '[' size = L_UINT ']'                            # ArrayInitialize
    | expression '[' index = expression ']'                       # ArrayAccess
    | '(' expression ')'                                          # Parenthetical
    | expression O_DOT (K_LENGTH | ID) ('(' call_parameters ')')? # DotOrFunctionCall
    | expression K_AS type                                        # Cast
    | op = O_UNARY value = expression                             # UnaryOp
    | a = expression op = O_MULTIPLICATIVE b = expression         # BinaryOp
    | a = expression op = O_ADDITIVE b = expression               # BinaryOp
    | a = expression op = O_COMPARISON b = expression             # BinaryOp
    | a = expression op = O_LOGICAL_AND b = expression            # BinaryOp
    | a = expression op = O_LOGICAL_OR b = expression             # BinaryOp
    ;
call_parameters: params += call_parameter? (',' params += call_parameter)*;
call_parameter:  (ID O_ASSIGN)? expression;

type:       K_INT | K_BOOL | K_FLOAT | K_STRING | ID;
literal:    L_BOOL | L_FLOAT | L_INT | L_STRING | L_NONE;
parameters: params += parameter? (',' params += parameter)*;
parameter:  type ID (O_ASSIGN value = literal)?;

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

fragment ALPHA:         [a-zA-Z];
fragment DIGIT:         [0-9];
fragment HEX_DIGIT:     DIGIT | A | B | C | D | E | F;
fragment STRING_ESCAPE: '\\n' | '\\t' | '\\\\' | '\\"';

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
L_BOOL:   K_TRUE | K_FALSE;
L_UINT:   DIGIT+ | '0' X HEX_DIGIT+;
L_INT:    O_NEGATIVE DIGIT+ | L_UINT;
L_FLOAT:  O_NEGATIVE? DIGIT+ O_DOT DIGIT+;
L_STRING: '"' (STRING_ESCAPE | .)? '"';
L_NONE:   N O N E;

// Flags (that are not also keywords)
F_HIDDEN:      H I D D E N;
F_CONDITIONAL: C O N D I T I O N A L;

// Notable identifiers
I_GET: G E T;
I_SET: S E T;

// Operators
O_DOT:              '.';
O_ASSIGN:           '=';
O_ASSIGN_ADD:       '+=';
O_ASSIGN_SUBTRACT:  '-=';
O_ASSIGN_MULTIPLY:  '*=';
O_ASSIGN_DIVIDE:    '/=';
O_ASSIGN_MODULO:    '%=';
O_NEGATIVE:         '-';
O_LOGICAL_NOT:      '!';
O_LOGICAL_OR:       '||';
O_LOGICAL_AND:      '&&';
O_MULTIPLY:         '*';
O_DIVIDE:           '/';
O_MODULO:           '%';
O_ADD:              '+';
O_SUBTRACT:         O_NEGATIVE;
O_EQUAL:            '==';
O_NOT_EQUAL:        '!=';
O_GREATER:          '>';
O_GREATER_OR_EQUAL: '>=';
O_LESS:             '<';
O_LESS_OR_EQUAL:    '<=';

O_UNARY:          O_NEGATIVE | O_LOGICAL_NOT;
O_MULTIPLICATIVE: O_MULTIPLY | O_DIVIDE | O_MODULO;
O_ADDITIVE:       O_ADD | O_SUBTRACT;
O_COMPARISON:     O_EQUAL | O_NOT_EQUAL | O_GREATER | O_GREATER_OR_EQUAL | O_LESS | O_LESS_OR_EQUAL;
O_ASSIGNMENT:     O_ASSIGN | O_ASSIGN_ADD | O_ASSIGN_SUBTRACT | O_ASSIGN_MULTIPLY | O_ASSIGN_DIVIDE | O_ASSIGN_MODULO;

NEWLINE:              '\n';
LINE_FEED:            '\r' -> skip;
LINE_BREAK:           '\\' NEWLINE -> skip; // Lines that end in "\" continue onto the next line.
WS:                   [ \t]+ -> skip;
LINE_COMMENT:         ';' .*? -> skip;
BLOCK_COMMENT:        ';/' .*? '/;' -> skip;
DOC_COMMENT:          '{' .*? '}';
LINE_END:             LINE_COMMENT? NEWLINE;
LINE_END_DOC_COMMENT: LINE_COMMENT? NEWLINE (DOC_COMMENT LINE_COMMENT NEWLINE)?;

ID: ALPHA (ALPHA | DIGIT)*;