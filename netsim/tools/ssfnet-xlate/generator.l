D			[0-9]
L			[a-zA-Z_]
H			[a-fA-F0-9]
E			[Ee][+-]?{D}+
FS			(f|F|l|L)
IS			(u|U|l|L)*

%{

/*
 * scehma.l :- used to construct configurable entities
 *
 */

#include <stdio.h>
#include <string.h>
#include "generator_parser.h"

%}

%START NORM CMT

%%
								{BEGIN NORM;}
<NORM>state_configuration		{ yylval.str=strdup(yytext); return STATE_CFG; }
<NORM>shared					{ yylval.str=strdup(yytext); return SHARED; }
<NORM>configurable				{ yylval.str=strdup(yytext); return CONFIGURABLE; }
<NORM>child_type				{ yylval.str=strdup(yytext); return CHILD_TYPE; }
<NORM>default_value				{ yylval.str=strdup(yytext); return DEFAULT_VALUE; }
<NORM>doc_string				{ yylval.str=strdup(yytext); return DOC_STR; }
<NORM>visualized                { yylval.str=strdup(yytext); return VISUALZIED; }
<NORM>statistic                 { yylval.str=strdup(yytext); return STATISTIC; }
<NORM>type						{ yylval.str=strdup(yytext); return TYPE; }
<NORM>unserialize_fct			{ yylval.str=strdup(yytext); return UNSERIALIZE_FCT; }
<NORM>serialize_fct				{ yylval.str=strdup(yytext); return SERIALIZE_FCT; }
<NORM>min_count					{ yylval.str=strdup(yytext); return MIN_COUNT; }
<NORM>max_count					{ yylval.str=strdup(yytext); return MAX_COUNT; }
<NORM>is_aliased				{ yylval.str=strdup(yytext); return IS_ALIASED; }
<NORM>__PRIME__T1__				{ yylval.str=strdup(yytext); return T1; }
<NORM>__PRIME__T2__				{ yylval.str=strdup(yytext); return T2; }
<NORM>__PRIME__T1_TPL__			{ yylval.str=strdup(yytext); return T1_TPL; }
<NORM>__PRIME__T2_TPL__			{ yylval.str=strdup(yytext); return T2_TPL; }
<NORM>__PRIME__TYPEID__			{ yylval.str=strdup(yytext); return TYPEID; }
<NORM>__PRIME__FILENAME__		{ yylval.str=strdup(yytext); return FILENAME; }
<NORM>__PRIME__ALIAS__			{ yylval.str=strdup(yytext); return ALIAS; }
<NORM>__PRIME__EMPTY__			{ yylval.str=strdup(yytext); return EMPTY; }

<NORM>unsigned					{ yylval.str=strdup(yytext); return UNSIGNED; }
<NORM>signed					{ yylval.str=strdup(yytext); return SIGNED; }
<NORM>double					{ yylval.str=strdup(yytext); return DOUBLE; }
<NORM>short						{ yylval.str=strdup(yytext); return SHORT; }
<NORM>float						{ yylval.str=strdup(yytext); return FLOAT; }
<NORM>void						{ yylval.str=strdup(yytext); return VOID; }
<NORM>char						{ yylval.str=strdup(yytext); return CHAR; }
<NORM>long[ \r\t\n]+long		{ yylval.str=strdup("long long"); return LONG_LONG; }
<NORM>long[ \r\t\n]+int			{ yylval.str=strdup("long int"); return LONG_INT; }
<NORM>long						{ yylval.str=strdup(yytext); return LONG; }
<NORM>int						{ yylval.str=strdup(yytext); return INT; }
<NORM>true						{ yylval.str=strdup(yytext); return CONSTANT; }
<NORM>false						{ yylval.str=strdup(yytext); return CONSTANT; }
<NORM>TRUE						{ yylval.str=strdup(yytext); return CONSTANT; }
<NORM>FALSE						{ yylval.str=strdup(yytext); return CONSTANT; }

<NORM>{L}({L}|{D})*				{ yylval.str=strdup(yytext); return IDENTIFIER; }

<NORM>0[xX]{H}+{IS}?			{ yylval.str=strdup(yytext); return CONSTANT; }
<NORM>0{D}+{IS}?				{ yylval.str=strdup(yytext); return CONSTANT; }
<NORM>{D}+{IS}?					{ yylval.str=strdup(yytext); return CONSTANT; }
<NORM>L?'(\\.|[^\\'])+'			{ yylval.str=strdup(yytext); return CONSTANT; }

<NORM>{D}+{E}{FS}?				{ yylval.str=strdup(yytext); return CONSTANT; }
<NORM>{D}*"."{D}+({E})?{FS}?	{ yylval.str=strdup(yytext); return CONSTANT; }
<NORM>{D}+"."{D}*({E})?{FS}?	{ yylval.str=strdup(yytext); return CONSTANT; }

<NORM>\"(\\.|[^\\"])*\"		{ yylval.str=strdup(yytext+1); yylval.str[strlen(yylval.str)-1]='\0'; return STRING_LITERAL; }

<NORM>"::"						{ yylval.str=strdup(yytext); return COLONS; }
<NORM>"{"						{ yylval.str=strdup(yytext); return LCURL; }
<NORM>"}"						{ yylval.str=strdup(yytext); return RCURL; }
<NORM>"<"						{ yylval.str=strdup(yytext); return LANGLE; }
<NORM>">"						{ yylval.str=strdup(yytext); return RANGLE; }
<NORM>";"						{ yylval.str=strdup(yytext); return SEMI; }
<NORM>"="						{ yylval.str=strdup(yytext); return EQ; }
<NORM>","						{ yylval.str=strdup(yytext); return COMMA; }
<NORM>"*"						{ yylval.str=strdup(yytext); return STAR; }
<NORM>"&"						{ yylval.str=strdup(yytext); return AMPR; }
<NORM>"//"[^\n]*[\n]			{ }
<NORM>"/*"						{ BEGIN CMT; }
<CMT>"*/"						{ BEGIN NORM; }
<CMT>.							{ }

[ \r\t\n]+						{ }
.								{ printf("\nERROR: skipping %s\n", yytext); }

%%

int lex_setup(const char* file)
{
  yyin = fopen(file, "r");
  if(!yyin) {
	fprintf(stderr, "cant open '%s'\n",file);
	exit(100);
  }
  yyrestart(yyin);
  return 0;
}

void lex_teardown()
{
  yy_delete_buffer(YY_CURRENT_BUFFER);
  fclose(yyin);
  yy_switch_to_buffer(0);
}
