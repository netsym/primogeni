/* A Bison parser, made by GNU Bison 2.3.  */

/* Skeleton interface for Bison's Yacc-like parsers in C

   Copyright (C) 1984, 1989, 1990, 2000, 2001, 2002, 2003, 2004, 2005, 2006
   Free Software Foundation, Inc.

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor,
   Boston, MA 02110-1301, USA.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* Tokens.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
   /* Put the tokens into the symbol table, so that GDB and other debuggers
      know about them.  */
   enum yytokentype {
     STATE_CFG = 258,
     SHARED = 259,
     CONFIGURABLE = 260,
     CHILD_TYPE = 261,
     UNSIGNED = 262,
     SIGNED = 263,
     DOUBLE = 264,
     SHORT = 265,
     FLOAT = 266,
     VOID = 267,
     CHAR = 268,
     LONG_LONG = 269,
     LONG_INT = 270,
     LONG = 271,
     INT = 272,
     IDENTIFIER = 273,
     CONSTANT = 274,
     STRING_LITERAL = 275,
     LCURL = 276,
     RCURL = 277,
     LANGLE = 278,
     RANGLE = 279,
     SEMI = 280,
     EQ = 281,
     COMMA = 282,
     STAR = 283,
     AMPR = 284,
     COLONS = 285,
     T1 = 286,
     T2 = 287,
     T1_TPL = 288,
     T2_TPL = 289,
     TYPEID = 290,
     FILENAME = 291,
     ALIAS = 292,
     EMPTY = 293,
     DEFAULT_VALUE = 294,
     DOC_STR = 295,
     TYPE = 296,
     UNSERIALIZE_FCT = 297,
     SERIALIZE_FCT = 298,
     VISUALZIED = 299,
     STATISTIC = 300,
     MIN_COUNT = 301,
     MAX_COUNT = 302,
     IS_ALIASED = 303
   };
#endif
/* Tokens.  */
#define STATE_CFG 258
#define SHARED 259
#define CONFIGURABLE 260
#define CHILD_TYPE 261
#define UNSIGNED 262
#define SIGNED 263
#define DOUBLE 264
#define SHORT 265
#define FLOAT 266
#define VOID 267
#define CHAR 268
#define LONG_LONG 269
#define LONG_INT 270
#define LONG 271
#define INT 272
#define IDENTIFIER 273
#define CONSTANT 274
#define STRING_LITERAL 275
#define LCURL 276
#define RCURL 277
#define LANGLE 278
#define RANGLE 279
#define SEMI 280
#define EQ 281
#define COMMA 282
#define STAR 283
#define AMPR 284
#define COLONS 285
#define T1 286
#define T2 287
#define T1_TPL 288
#define T2_TPL 289
#define TYPEID 290
#define FILENAME 291
#define ALIAS 292
#define EMPTY 293
#define DEFAULT_VALUE 294
#define DOC_STR 295
#define TYPE 296
#define UNSERIALIZE_FCT 297
#define SERIALIZE_FCT 298
#define VISUALZIED 299
#define STATISTIC 300
#define MIN_COUNT 301
#define MAX_COUNT 302
#define IS_ALIASED 303




#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef union YYSTYPE
#line 23 "/Users/liux/Workspace/primogeni/netsim/tools/ssfnet-xlate/generator.y"
{
	char                 * str;
	struct config_type   * ctype_ptr;
	struct config_var    * var_ptr;
	struct config_child  * child_ptr;
}
/* Line 1529 of yacc.c.  */
#line 152 "y.tab.h"
	YYSTYPE;
# define yystype YYSTYPE /* obsolescent; will be withdrawn */
# define YYSTYPE_IS_DECLARED 1
# define YYSTYPE_IS_TRIVIAL 1
#endif

extern YYSTYPE yylval;

