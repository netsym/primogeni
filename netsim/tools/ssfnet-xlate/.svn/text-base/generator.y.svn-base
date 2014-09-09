%{
#include <stdio.h>
#include <code_generator.h>
#include <string.h>
void yyerror(const char *str);
extern "C"
{
    int yyparse(void);
    int yylex(void);  
	int lex_setup(const char* file);
	void lex_teardown();
    int yywrap()
        {
                return 1;
        }

}
extern int yydebug; 
struct config_type   * cur_ctype;
struct config_var    * cur_cvar;
struct config_child  * cur_cchild;
%}
%union {
	char                 * str;
	struct config_type   * ctype_ptr;
	struct config_var    * var_ptr;
	struct config_child  * child_ptr;
}

%token <str> STATE_CFG
%token <str> SHARED
%token <str> CONFIGURABLE
%token <str> CHILD_TYPE
%token <str> UNSIGNED
%token <str> SIGNED
%token <str> DOUBLE
%token <str> SHORT
%token <str> FLOAT
%token <str> VOID
%token <str> CHAR
%token <str> LONG_LONG
%token <str> LONG_INT
%token <str> LONG
%token <str> INT
%token <str> IDENTIFIER
%token <str> CONSTANT
%token <str> STRING_LITERAL
%token <str> LCURL
%token <str> RCURL
%token <str> LANGLE
%token <str> RANGLE
%token <str> SEMI
%token <str> EQ
%token <str> COMMA
%token <str> STAR
%token <str> AMPR
%token <str> COLONS
%token <str> T1
%token <str> T2
%token <str> T1_TPL
%token <str> T2_TPL
%token <str> TYPEID
%token <str> FILENAME
%token <str> ALIAS
%token <str> EMPTY
%token <str> DEFAULT_VALUE
%token <str> DOC_STR
%token <str> TYPE
%token <str> UNSERIALIZE_FCT
%token <str> SERIALIZE_FCT
%token <str> VISUALZIED
%token <str> STATISTIC;
%token <str> MIN_COUNT
%token <str> MAX_COUNT
%token <str> IS_ALIASED

%type <str> type
%type <str> tplparams
%type <str> value
%type <str> identifier
%type <str> scopped_identifier
%type <str> scopped_type
%type <str> class_name
%type <str> extends_name
%type <str> class_tpl
%type <str> extends_tpl
%type <str> file_name
%type <str> alias
%type <str> type_id


%type <ctype_ptr> state_configs
%type <ctype_ptr> state_config 
%type <ctype_ptr> state_config_inner

%type <var_ptr> variable
%type <var_ptr> var_prefix
%type <var_ptr> variable_inner

%type <child_ptr> child_type
%type <child_ptr> child_type_inner

%start state_configs

%%

state_configs: state_config SEMI state_configs  
				| state_config  SEMI 
				;
				
state_config: STATE_CFG LCURL class_name extends_name class_tpl extends_tpl type_id alias file_name 
			  {cur_ctype=addConfigType($3, $5, $4, $6, $7, $8, $9); }
			  state_config_inner RCURL {$$ = getHead(); } ;
				
class_name: T1 EQ STRING_LITERAL SEMI { $$=$3; };

extends_name: T2 EQ STRING_LITERAL SEMI {$$=$3; };

class_tpl: T1_TPL EQ STRING_LITERAL SEMI { $$=$3; };

extends_tpl: T2_TPL EQ STRING_LITERAL SEMI {$$=$3; };

file_name: FILENAME EQ STRING_LITERAL SEMI { $$=$3; };

type_id: TYPEID EQ STRING_LITERAL SEMI { $$=$3; };

alias: ALIAS EQ STRING_LITERAL SEMI { $$=$3; };

state_config_inner: variable SEMI { $$ = 0;}
				| child_type SEMI { $$ = 0;}
				| EMPTY SEMI { $$ = 0;}
				| state_config_inner variable SEMI  { $$ = 0;}
				| state_config_inner child_type SEMI { $$ = 0;}
				;
identifier: IDENTIFIER
		| DEFAULT_VALUE
		| DOC_STR
		| TYPE
		| UNSERIALIZE_FCT
		| SERIALIZE_FCT
		| MIN_COUNT
		| MAX_COUNT
		| IS_ALIASED
        | identifier LANGLE tplparams RANGLE {$$ = (char*)malloc(strlen($1)+strlen($2)+6); sprintf($$,"%s < %s >",$1,$3); }
        | identifier STAR {$$ = (char*)malloc(strlen($1)+3); sprintf($$,"%s *",$1); }
        | identifier AMPR {$$ = (char*)malloc(strlen($1)+3); sprintf($$,"%s &",$1); }
		;

tplparams: scopped_identifier COMMA tplparams {$$ = (char*)malloc(strlen($1)+strlen($3)+3); sprintf($$,"%s, %s",$1,$3); }
		| scopped_identifier
		;
	
type: identifier
    | UNSIGNED LONG_LONG {$$ = (char*)malloc(strlen($1)+strlen($2)+2); sprintf($$,"%s %s",$1,$2); }
    | UNSIGNED LONG_INT {$$ = (char*)malloc(strlen($1)+strlen($2)+2); sprintf($$,"%s %s",$1,$2); }
	| UNSIGNED LONG {$$ = (char*)malloc(strlen($1)+strlen($2)+2); sprintf($$,"%s %s",$1,$2); }
	| UNSIGNED SHORT {$$ = (char*)malloc(strlen($1)+strlen($2)+2); sprintf($$,"%s %s",$1,$2); }
	| UNSIGNED CHAR {$$ = (char*)malloc(strlen($1)+strlen($2)+2); sprintf($$,"%s %s",$1,$2); }
	| UNSIGNED INT {$$ = (char*)malloc(strlen($1)+strlen($2)+2); sprintf($$,"%s %s",$1,$2); }
	| SIGNED LONG_LONG {$$ = (char*)malloc(strlen($1)+strlen($2)+2); sprintf($$,"%s %s",$1,$2); }
	| SIGNED LONG_INT {$$ = (char*)malloc(strlen($1)+strlen($2)+2); sprintf($$,"%s %s",$1,$2); }
	| SIGNED LONG {$$ = (char*)malloc(strlen($1)+strlen($2)+2); sprintf($$,"%s %s",$1,$2); }
	| SIGNED SHORT {$$ = (char*)malloc(strlen($1)+strlen($2)+2); sprintf($$,"%s %s",$1,$2); }
	| SIGNED CHAR {$$ = (char*)malloc(strlen($1)+strlen($2)+2); sprintf($$,"%s %s",$1,$2); }
	| SIGNED INT {$$ = (char*)malloc(strlen($1)+strlen($2)+2); sprintf($$,"%s %s",$1,$2); }
	| LONG_LONG
	| LONG_INT
	| LONG
	| DOUBLE
	| SHORT
	| FLOAT
	| VOID
	| CHAR
	| INT
	;
	
scopped_identifier: scopped_identifier COLONS identifier  {$$ = (char*)malloc(strlen($1)+strlen($3)+3); sprintf($$,"%s::%s",$1,$3); }
			| identifier
			;
	
scopped_type: scopped_type COLONS type  {$$ = (char*)malloc(strlen($1)+strlen($3)+3); sprintf($$,"%s::%s",$1,$3); }
			| type
			;

value: scopped_identifier | CONSTANT | STRING_LITERAL ;

var_prefix: CONFIGURABLE SHARED scopped_type identifier { $$ = addConfigVar(cur_ctype, 1, 1, 0, 0, 0, $3, $4, 0, 0, 0, 0, 0); }
		| SHARED CONFIGURABLE scopped_type identifier { $$ = addConfigVar(cur_ctype, 1, 1, 0, 0, 0, $3, $4, 0, 0, 0, 0, 0); }
		| CONFIGURABLE scopped_type identifier { $$ = addConfigVar(cur_ctype, 0, 1, 0, 0, 0, $2, $3, 0, 0, 0, 0, 0); }
		| SHARED scopped_type identifier { $$ = addConfigVar(cur_ctype, 1, 0, 0, 0, 0, $2, $3, 0, 0, 0, 0, 0); }
		| scopped_type identifier { $$ = addConfigVar(cur_ctype, 0, 0, 0, 0, 0, $1, $2, 0, 0, 0, 0, 0); }

variable: var_prefix LCURL {cur_cvar=$1;} variable_inner RCURL 
		{ 
			$$ = cur_cvar;
			if(0==$$->config_type) {
				figure_out_config_type($$);
			}
		};
		| var_prefix EQ value
		{
			$1->raw_string=strdup($3);
			$$ = $1;
			if(0==$$->config_type) {
				figure_out_config_type($$);
			}
		};
		| var_prefix
		{
			$$ = $1;
			if(0==$$->config_type) {
				figure_out_config_type($$);
			}
		};

variable_inner: DEFAULT_VALUE EQ value SEMI { cur_cvar->default_value=strdup($3); $$ = cur_cvar; }
			| DOC_STR  EQ STRING_LITERAL SEMI  { cur_cvar->doc_string=strdup($3); $$ = cur_cvar; }
			| TYPE  EQ IDENTIFIER SEMI  { cur_cvar->config_type=strdup($3); $$ = cur_cvar; }
			| UNSERIALIZE_FCT EQ scopped_identifier SEMI  { cur_cvar->unserialize_fct=strdup($3); $$ = cur_cvar; }
			| SERIALIZE_FCT  EQ scopped_identifier SEMI  { cur_cvar->serialize_fct=strdup($3); $$ = cur_cvar; }
			| VISUALZIED EQ CONSTANT SEMI  {cur_cvar->is_visualized=(strcmp($3,"true")==0)?1:(strcmp($3,"TRUE")==0?1:0); $$ = cur_cvar; }
			| STATISTIC EQ CONSTANT SEMI  {cur_cvar->is_stat=(strcmp($3,"true")==0)?1:(strcmp($3,"TRUE")==0?1:0); $$ = cur_cvar; }
			| variable_inner DEFAULT_VALUE EQ value SEMI { cur_cvar->default_value=strdup($4); $$ = cur_cvar; }
			| variable_inner DOC_STR  EQ STRING_LITERAL SEMI  { cur_cvar->doc_string=strdup($4); $$ = cur_cvar; } 
			| variable_inner TYPE  EQ IDENTIFIER SEMI  { cur_cvar->config_type=strdup($4); $$ = cur_cvar; }
			| variable_inner UNSERIALIZE_FCT EQ scopped_identifier SEMI  { cur_cvar->unserialize_fct=strdup($4); $$ = cur_cvar; }
			| variable_inner SERIALIZE_FCT  EQ scopped_identifier SEMI  { cur_cvar->serialize_fct=strdup($4); $$ = cur_cvar; }
			| variable_inner VISUALZIED EQ CONSTANT SEMI  { cur_cvar->is_visualized=(strcmp($4,"true")==0)?1:(strcmp($4,"TRUE")==0?1:0); $$ = cur_cvar; }
			| variable_inner STATISTIC EQ CONSTANT SEMI  { cur_cvar->is_stat=(strcmp($4,"true")==0)?1:(strcmp($4,"TRUE")==0?1:0); $$ = cur_cvar; }
			;

child_type: CHILD_TYPE LANGLE scopped_identifier RANGLE IDENTIFIER LCURL 
			{ cur_cchild =  addConfigChild(cur_ctype, $3, $5, 0, 0, 0, 0 ); }
			child_type_inner RCURL { $$ = cur_cchild; };

child_type_inner: MIN_COUNT EQ CONSTANT SEMI  { cur_cchild->min=strdup($3); $$ = 0; }
			| MAX_COUNT  EQ CONSTANT SEMI  { cur_cchild->max=strdup($3); $$ = 0; }
			| IS_ALIASED  EQ value SEMI  {
				cur_cchild->is_aliased=false;
				if(0!=strcasecmp("false",$3) && 0!=strcmp("0",$3)) {
					cur_cchild->is_aliased=true;
				}
				$$ = 0;
			}
			| DOC_STR  EQ STRING_LITERAL SEMI  { cur_cchild->doc_string=strdup($3); $$ = 0; }
			| child_type_inner MIN_COUNT EQ CONSTANT SEMI  { cur_cchild->min=strdup($4); $$ = 0; }
			| child_type_inner MAX_COUNT  EQ CONSTANT SEMI  { cur_cchild->max=strdup($4); $$ = 0; }
			| child_type_inner IS_ALIASED  EQ value SEMI  {
				cur_cchild->is_aliased=false;
				if(0!=strcasecmp("false",$4) && 0!=strcmp("0",$4)) {
					cur_cchild->is_aliased=true;
				}
				$$ = 0;
			}
			| child_type_inner DOC_STR  EQ STRING_LITERAL SEMI  { cur_cchild->doc_string=strdup($4); $$ = 0; }
			;
%%
void yyerror(const char *str)
{
        fprintf(stderr,"error: %s\n",str);
}
   
int main(int argc, char** argv)
{
	yydebug=0;
	//yydebug=1;
	return __main__(argc,argv);
}
