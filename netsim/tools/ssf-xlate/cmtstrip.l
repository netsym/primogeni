%{
/*
 * cmtstrip.l :- lex scanner used for stripping comments except ssf 
 *               connotations.
 */

#include <stdio.h>
/*int line_number = 1;*/

int yywrap();
%}

%START NORM QUO CMT0 CMT1 CMT2 CMT3 CMT4

%%

			{ BEGIN NORM; }

<NORM>\"		{ BEGIN QUO; fprintf(yyout,"\""); }
<QUO>\"			{ BEGIN NORM; fprintf(yyout,"\""); }
<QUO>\n			{ /*++line_number;*/ fprintf(yyout,"\n"); }
<QUO>\\\"		{ fprintf(yyout,"\\\""); }

<NORM>"/*"		{ BEGIN CMT0; fprintf(yyout,"  "); }
<CMT0>"*"		{ BEGIN CMT1; fprintf(yyout," "); }
<CMT0>\n		{ /*++line_number;*/ fprintf(yyout,"\n"); }
<CMT0>\t		{ fprintf(yyout,"\t"); }
<CMT0>[^*\t\n]		{ fprintf(yyout," "); }
<CMT1>"/"		{ BEGIN NORM; fprintf(yyout," "); }
<CMT1>"*"		{ fprintf(yyout," "); }
<CMT1>\n		{ /*++line_number;*/ BEGIN CMT0; fprintf(yyout,"\n"); }
<CMT1>\t		{ BEGIN CMT0; fprintf(yyout,"\t"); }
<CMT1>[^*/\t\n]		{ BEGIN CMT0; fprintf(yyout," "); }

<NORM>"//"              { BEGIN CMT2; }
<CMT2>!			{ BEGIN CMT3; fprintf(yyout,"//!"); }
<CMT2>\n		{ /*++line_number;*/ BEGIN NORM; fprintf(yyout,"\n"); }
<CMT2>[^!\n]		{ BEGIN CMT4; }
<CMT3>\n		{ /*++line_number;*/ BEGIN NORM; fprintf(yyout,"\n"); }
<CMT3>[^\n]		{ fprintf(yyout,"%s", yytext); }
<CMT4>\n		{ /*++line_number;*/ BEGIN NORM; fprintf(yyout,"\n"); }
<CMT4>[^\n]		;

.			{ fprintf(yyout,"%s", yytext); }

%%

int main(int argc, char** argv)
{
  yyin = stdin;
  yyout = stdout;

  yylex();

  return 0;
}

int yywrap() { return 1; }
