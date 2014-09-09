%{
/*
 * extract.l :- used to extract the configurable entity
 *              declarations from ssfnet configurable entities 
 *
 * Below is a basic statemap that describes how the state configuration is extracted:
 * ...                                [SE0]
 * class                              [SEO->SE1]
 * <whitespace>                       [SE1]
 * <class name>                       [SE1->SE2]
 * <whitespace>                       [SE2]
 * :                                  [SE2->SE3]
 * ...                                [SE3]
 * ConfigurableEntity                 [SE3->SE4]
 * % we then parse either:
 * %   <T1>                      [SE4->SE5->SE6->SE11]
 * %   or
 * %   <T1,T2>                   [SE4->SE5->SE6->SE7->SE8->SE11]
 * %   or
 * %   <T1,T2,type_id>           [SE4->SE5->SE6->SE7->SE8->SE9->SE10->SE11]
 * %   <T1,T2,type_id, alias>    [SE4->SE5->SE6->SE7->SE8->SE9->SE9a->SE9b->SE10->SE11]
 * <whitespace or other extends>      [SE11]
 * {                                  [SE11->SE12]
 * state_configuration                [SE12->SE13]
 * {                                  [SE13->SE14]
 * ...								   [SE14]
 * }                                  [SE14->SE0]
 * ...								   [SE0]
 */

#include <stdio.h>
#include <string.h>
char t1[256], t1_tpl[256], t2[256], t2_tpl[256], type[256], alias[256], filename[512];
int cls_ocurl, ce_ocurl, tpos;
FILE* fp;

int yywrap();

%}

%START SE0 SE1 SE2 SE3 SE4 SE5 SE6 SE6A SE6B SE7 SE8 SE8A SE8B SE9 SE10 SE11 SE12 SE13 SE14 SE9a SE9b

%%

							{ BEGIN SE0; }

<SE0>class					{ BEGIN SE1; fprintf(yyout,"%s", yytext); }
<SE0>[ \r\t\n]+				{ fprintf(yyout,"%s", yytext); }
<SE0>.						{ fprintf(yyout,"%s", yytext); }

<SE1>[a-zA-Z_][a-zA-Z0-9_]*	{ BEGIN SE2; fprintf(yyout,"%s", yytext);
							  snprintf(t1,256,"%s",yytext);
							  t2[0]='\0';
							  t1_tpl[0]='\0';
							  t2_tpl[0]='\0';
							  type[0]='\0';
							  alias[0]='\0';
							  cls_ocurl=0;
							}
<SE1>[ \r\t\n]+				{ fprintf(yyout,"%s", yytext); }
<SE1>.						{ BEGIN SE0; fprintf(yyout,"%s", yytext); }

<SE2>":"					{ BEGIN SE3; fprintf(yyout,"%s", yytext); }
<SE2>[ \r\t\n]+				{ fprintf(yyout,"%s", yytext); }
<SE2>.						{ BEGIN SE0; fprintf(yyout,"%s", yytext); }

<SE3>ConfigurableEntity		{ BEGIN SE4; fprintf(yyout,"%s", yytext); }
<SE3>"{"					{ BEGIN SE0; fprintf(yyout,"%s", yytext); }
<SE3>.						{ fprintf(yyout,"%s", yytext); }

<SE4>"<"					{ BEGIN SE5; fprintf(yyout,"%s", yytext); }
<SE4>[ \r\t\n]+				{ fprintf(yyout,"%s", yytext); }		
<SE4>.						{ BEGIN SE0; fprintf(yyout,"%s", yytext); }

<SE5>[a-zA-Z_][a-zA-Z0-9_]* { fprintf(yyout,"%s", yytext);
						  	  if(strlen(t1)!=strlen(yytext)) { BEGIN SE0; }
							  else if(strcmp(t1,yytext)) { BEGIN SE0; }
							  else { BEGIN SE6; }
							}
<SE5>[ \r\t\n]+				{ fprintf(yyout,"%s", yytext); }		
<SE5>.						{ BEGIN SE0; fprintf(yyout,"%s", yytext); }


<SE6>","					{ BEGIN SE7; fprintf(yyout,"%s", yytext); }
<SE6>">"					{ BEGIN SE11; fprintf(yyout,"%s", yytext); }
<SE6>[ \r\t\n]+				{ fprintf(yyout,"%s", yytext); }		
<SE6>"<"					{
								BEGIN SE6A;
								fprintf(yyout,"%s", yytext);
								tpos=0;
							}
<SE6>.						{ BEGIN SE0; fprintf(yyout,"%s", yytext); }

<SE6A>">"					{
								BEGIN SE6B;
								fprintf(yyout,"%s", yytext);
								t1_tpl[tpos++]='\0';
							}
<SE6A>.						{ fprintf(yyout,"%s", yytext); t1_tpl[tpos++]=yytext[0]; }		

<SE6B>","					{ BEGIN SE7; fprintf(yyout,"%s", yytext); }
<SE6B>">"					{ BEGIN SE11; fprintf(yyout,"%s", yytext); }
<SE6B>[ \r\t\n]+				{ fprintf(yyout,"%s", yytext); }
<SE6B>.						{ BEGIN SE0; fprintf(yyout,"%s", yytext); }


<SE7>[a-zA-Z_][a-zA-Z0-9_]* { BEGIN SE8; fprintf(yyout,"%s", yytext);
							  snprintf(t2,256,"%s",yytext);
							}
<SE7>[ \r\t\n]+				{ fprintf(yyout,"%s", yytext); }		
<SE7>.						{ BEGIN SE0; fprintf(yyout,"%s", yytext); }


<SE8>","					{ BEGIN SE9; fprintf(yyout,"%s", yytext); }
<SE8>">"					{ BEGIN SE11; fprintf(yyout,"%s", yytext);  }
<SE8>"<"					{
								BEGIN SE8A;
								fprintf(yyout,"%s", yytext);
								tpos=0;
							}

<SE8>[ \r\t\n]+				{ fprintf(yyout,"%s", yytext); }		
<SE8>.						{ BEGIN SE0; fprintf(yyout,"%s", yytext); }

<SE8A>">"					{
								BEGIN SE8B;
								fprintf(yyout,"%s", yytext);
								t2_tpl[tpos++]='\0';
							}
<SE8A>.						{ fprintf(yyout,"%s", yytext); t2_tpl[tpos++]=yytext[0];}		

<SE8B>","					{ BEGIN SE9; fprintf(yyout,"%s", yytext); }
<SE8B>">"					{ BEGIN SE11; fprintf(yyout,"%s", yytext); }
<SE8B>[ \r\t\n]+			{ fprintf(yyout,"%s", yytext); }
<SE8B>.						{ BEGIN SE0; fprintf(yyout,"%s", yytext); }


<SE9>[ \r\t\n]+				{ fprintf(yyout,"%s", yytext); }		
<SE9>[^>,]+					{ BEGIN SE9a; fprintf(yyout,"%s", yytext); 
							  snprintf(type,256,"%s",yytext);
							}
<SE9>.						{ BEGIN SE0; fprintf(yyout,"%s", yytext); }

<SE9a>","					{ BEGIN SE9b; }
<SE9a>">"					{ BEGIN SE11; fprintf(yyout,"%s", yytext);  }
<SE9a>[ \r\t\n]+			{ fprintf(yyout,"%s", yytext); }		
<SE9a>.						{ BEGIN SE0; fprintf(yyout,"%s", yytext); }

<SE9b>[ \r\t\n]+			{ }		
<SE9b>[^>]+					{ BEGIN SE10; snprintf(alias,256,"%s",yytext); }
<SE9b>.						{ }

<SE10>">"					{ BEGIN SE11; fprintf(yyout,"%s", yytext); }
<SE10>[ \r\t\n]+			{ fprintf(yyout,"%s", yytext); }		
<SE10>.						{ BEGIN SE0; fprintf(yyout,"%s", yytext); }




<SE11>"{"					{ BEGIN SE12; fprintf(yyout,"%s", yytext); cls_ocurl=1;}
<SE11>"}"					{ BEGIN SE0; fprintf(yyout,"%s", yytext); }
<SE11>.						{ fprintf(yyout,"%s", yytext); }

<SE12>"state_configuration"	{ BEGIN SE13; fprintf(fp,"%s", yytext); }
<SE12>"{"					{ cls_ocurl++; fprintf(yyout,"%s", yytext); }
<SE12>"}"					{	cls_ocurl--;
								if(cls_ocurl==0) { 
									//the config entity did not have a state_configuration but
									//it extends another config entity (look at routers)
									BEGIN SE0;
									fprintf(fp,"\nstate_configuration {");
									fprintf(fp,"\n\t__PRIME__T1__=\"%s\";",t1);
									fprintf(fp,"\n\t__PRIME__T2__=\"%s\";",t2);
									fprintf(fp,"\n\t__PRIME__T1_TPL__=\"%s\";",t1_tpl);
									fprintf(fp,"\n\t__PRIME__T2_TPL__=\"%s\";",t2_tpl);
									fprintf(fp,"\n\t__PRIME__TYPEID__=\"%s\";",type);
									if(strlen(alias)==0) sprintf(alias,"\"\"");
									fprintf(fp,"\n\t__PRIME__ALIAS__=%s;",alias);
									fprintf(fp,"\n\t__PRIME__FILENAME__=\"%s\";\n",filename);
									fprintf(fp,"\n\t__PRIME__EMPTY__;\n");
									fprintf(fp,"\t};\n");
								  	fprintf(yyout,"\n/* SSFNET_XLATE_EXTRACTION_POINT class=\"%s\" */\n", t1);

								}
								fprintf(yyout,"%s", yytext); 
							}
<SE12>.						{ fprintf(yyout,"%s", yytext); }

<SE13>"{"					{ BEGIN SE14; fprintf(fp,"%s", yytext); 
							  ce_ocurl=1; 
							  fprintf(fp,"\n\t__PRIME__T1__=\"%s\";",t1);
							  fprintf(fp,"\n\t__PRIME__T2__=\"%s\";",t2);
							  fprintf(fp,"\n\t__PRIME__T1_TPL__=\"%s\";",t1_tpl);
							  fprintf(fp,"\n\t__PRIME__T2_TPL__=\"%s\";",t2_tpl);
							  fprintf(fp,"\n\t__PRIME__TYPEID__=\"%s\";",type);
							  if(strlen(alias)==0) sprintf(alias,"\"\"");
							  fprintf(fp,"\n\t__PRIME__ALIAS__=%s;",alias);
							  fprintf(fp,"\n\t__PRIME__FILENAME__=\"%s\";\n",filename);
							}
<SE13>[ \r\t\n]+			{ fprintf(fp,"%s", yytext); }
<SE13>.						{ BEGIN SE0;
							  snprintf(t1,256,"\n/* Error while extract state_configuration*/\n"); 
							  fprintf(fp,"%s",t1);
							  fprintf(yyout,"%s",t1);
							}
<SE14>"{"					{ ce_ocurl++; fprintf(fp,"%s", yytext); }
<SE14>"}"					{ ce_ocurl--; fprintf(fp,"%s", yytext);
							  if(ce_ocurl==0) { 
								  	BEGIN SE0;
								  	fprintf(fp,";\n");
								  	fprintf(yyout,"\n/* SSFNET_XLATE_EXTRACTION_POINT class=\"%s\" */\n", t1);
							  }
							}
<SE14>[ \r\t\n]+			{ fprintf(fp,"%s", yytext); }
<SE14>.						{ fprintf(fp,"%s", yytext); }

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

int yywrap() { return 1; }

int main(int argc, char** argv)
{
	if(argc != 2) {
		fprintf(stderr,"Must specify the source .m file!");
		exit(100);
	}
	if(lex_setup(argv[1])) return 100;
	argv[1][strlen(argv[1])-1]='h';
	snprintf(filename,512,"%s",argv[1]);
	fp=stdout;
 	yyout = fopen(argv[1],"w");
 	if(yyout==NULL) {
 		fprintf(stderr, "Error opening '%s'\n", argv[1]);
 		exit(100); 
 	}
 	fprintf(yyout,"/* ------------------------- */\n");
 	fprintf(yyout,"/* ------------------------- */\n");
 	fprintf(yyout,"/*         WARNING: */\n");
 	fprintf(yyout,"/*  THIS FILE IS GENERATED! */\n");
 	fprintf(yyout,"/*        DO NOT EDIT! */\n");
 	fprintf(yyout,"/* ------------------------- */\n");
 	fprintf(yyout,"/* ------------------------- */\n\n");
	yylex();
	fclose(yyout);
	lex_teardown();
	return 0;
}
