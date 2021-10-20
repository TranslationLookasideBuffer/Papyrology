grammar Papyrus
    ;

script: NEWLINE* header (DOC_COMMENT NEWLINE)? script_statement*;

header: K_SCRIPT_NAME ID (K_EXTENDS ID)? script_flag* NEWLINE;

script_flag: F_HIDDEN | F_CONDITIONAL;

script_statement: import_statement | script_variable_statement | property_statement | state | function | event;

import_statement: K_IMPORT ID NEWLINE;

script_variable_statement: type ID ('=' literal) variable_flag* NEWLINE;

variable_flag: F_CONDITIONAL;

type: K_INT | K_BOOL | K_FLOAT | K_STRING | ID;

literal: L_BOOL | L_FLOAT | L_INT | L_STRING | L_NONE;

property_statement
    : type K_PROPERTY ID F_HIDDEN? NEWLINE property_function property_function? K_END_PROPERTY NEWLINE # Full
    | type K_PROPERTY ID ('=' literal)? K_AUTO F_HIDDEN? NEWLINE                                       # Auto
    | type K_PROPERTY ID '=' literal K_AUTO_READ_ONLY F_HIDDEN? NEWLINE                                # AutoReadOnly
    | type K_PROPERTY ID '=' literal (K_AUTO | K_AUTO_READ_ONLY) F_CONDITIONAL NEWLINE                 # Conditional
    ;
property_function
    : type K_FUNCTION I_GET '(' ')' NEWLINE function_statement* K_END_FUNCTION NEWLINE           # Get
    | K_FUNCTION I_SET '(' function_param ')' NEWLINE function_statement* K_END_FUNCTION NEWLINE # Set
    ;

function: type? K_FUNCTION ID '(' function_params ')' function_flag* NEWLINE (function_statement* K_END_FUNCTION NEWLINE)?;

function_params: function_param? (',' function_param)*;
function_param:  type ID ('=' literal)?;
function_flag:   K_NATIVE | K_GLOBAL;

function_statement
    : type ID ('=' expression)?                                                                                                                              # Define
    | function_assign_value op = ('=' | '+=' | '-=' | '*=' | '/=' | '%=') expression NEWLINE                                                                 # Assign
    | K_RETURN expression? NEWLINE                                                                                                                           # Return
    | K_IF expression NEWLINE function_statement* (K_ELSE_IF expression NEWLINE function_statement*)? (K_ELSE NEWLINE function_statement*)? K_END_IF NEWLINE # If
    | K_WHILE expression NEWLINE function_statement* K_END_WHILE NEWLINE                                                                                     # While
    ;
function_assign_value: ID | expression '.' ID | expression '[' expression ']';

expression
    : ID                                                                 # ID
    | K_NEW type '[' size = L_UINT ']'                                   # ArrayInitialize
    | expression '[' index = expression ']'                              # ArrayAccess
    | '(' expression ')'                                                 # Parenthetical
    | expression '.' (K_LENGTH | ID) ('(' function_call_params ')')?     # DotOrFunctionCall
    | expression K_AS type                                               # Cast
    | op = ( '-' | '!') expression                                       # UnaryOp
    | expression op = ( '*' | '/' | '%') expression                      # BinaryOp
    | expression op = ( '+' | '-') expression                            # BinaryOp
    | expression op = ('==' | '!=' | '>' | '>=' | '<' | '<=') expression # BinaryOp
    | expression op = '&&' expression                                    # BinaryOp
    | expression op = '||' expression                                    # BinaryOp
    ;

function_call_params: function_call_param? (',' function_call_param)*;
function_call_param:  (ID '=')? expression;

state: K_AUTO? K_STATE ID NEWLINE (function | event)* K_END_STATE NEWLINE;

event: K_EVENT ID '(' function_params ')' K_NATIVE? NEWLINE (function_statement* K_END_EVENT NEWLINE)?;

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
L_INT:    '-' DIGIT+ | L_UINT;
L_FLOAT:  '-'? DIGIT+ '.' DIGIT+;
L_STRING: '"' (STRING_ESCAPE | .)? '"';
L_NONE:   N O N E;

// Flags (that are not also keywords)
F_HIDDEN:      H I D D E N;
F_CONDITIONAL: C O N D I T I O N A L;

// Notable identifiers
I_GET : G E T;
I_SET : S E T;

NEWLINE:       '\n';
LINE_FEED:     '\r';
LINE_BREAK:    '\\' NEWLINE -> skip; // Lines that end in "\" continue onto the next line.
WS:            [ \t]+ -> skip;
LINE_COMMENT:  ';' .*? NEWLINE -> skip;
BLOCK_COMMENT: ';/' .*? '/;' -> skip;
DOC_COMMENT:   '{' .*? '}';

ID: ALPHA (ALPHA | DIGIT)*;