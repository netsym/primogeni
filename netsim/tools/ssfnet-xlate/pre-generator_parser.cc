/* A Bison parser, made by GNU Bison 3.0.2.  */

/* Bison implementation for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2013 Free Software Foundation, Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

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

/* C LALR(1) parser skeleton written by Richard Stallman, by
   simplifying the original so-called "semantic" parser.  */

/* All symbols defined below should begin with yy or YY, to avoid
   infringing on user name space.  This should be done even for local
   variables, as they might otherwise be expanded by user macros.
   There are some unavoidable exceptions within include files to
   define necessary library symbols; they are noted "INFRINGES ON
   USER NAME SPACE" below.  */

/* Identify Bison output.  */
#define YYBISON 1

/* Bison version.  */
#define YYBISON_VERSION "3.0.2"

/* Skeleton name.  */
#define YYSKELETON_NAME "yacc.c"

/* Pure parsers.  */
#define YYPURE 0

/* Push parsers.  */
#define YYPUSH 0

/* Pull parsers.  */
#define YYPULL 1




/* Copy the first part of user declarations.  */
#line 1 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:339  */

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

#line 89 "y.tab.c" /* yacc.c:339  */

# ifndef YY_NULLPTR
#  if defined __cplusplus && 201103L <= __cplusplus
#   define YY_NULLPTR nullptr
#  else
#   define YY_NULLPTR 0
#  endif
# endif

/* Enabling verbose error messages.  */
#ifdef YYERROR_VERBOSE
# undef YYERROR_VERBOSE
# define YYERROR_VERBOSE 1
#else
# define YYERROR_VERBOSE 0
#endif

/* In a future release of Bison, this section will be replaced
   by #include "y.tab.h".  */
#ifndef YY_YY_Y_TAB_H_INCLUDED
# define YY_YY_Y_TAB_H_INCLUDED
/* Debug traces.  */
#ifndef YYDEBUG
# define YYDEBUG 1
#endif
#if YYDEBUG
extern int yydebug;
#endif

/* Token type.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
  enum yytokentype
  {
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

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef union YYSTYPE YYSTYPE;
union YYSTYPE
{
#line 23 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:355  */

	char                 * str;
	struct config_type   * ctype_ptr;
	struct config_var    * var_ptr;
	struct config_child  * child_ptr;

#line 232 "y.tab.c" /* yacc.c:355  */
};
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;

int yyparse (void);

#endif /* !YY_YY_Y_TAB_H_INCLUDED  */

/* Copy the second part of user declarations.  */

#line 247 "y.tab.c" /* yacc.c:358  */

#ifdef short
# undef short
#endif

#ifdef YYTYPE_UINT8
typedef YYTYPE_UINT8 yytype_uint8;
#else
typedef unsigned char yytype_uint8;
#endif

#ifdef YYTYPE_INT8
typedef YYTYPE_INT8 yytype_int8;
#else
typedef signed char yytype_int8;
#endif

#ifdef YYTYPE_UINT16
typedef YYTYPE_UINT16 yytype_uint16;
#else
typedef unsigned short int yytype_uint16;
#endif

#ifdef YYTYPE_INT16
typedef YYTYPE_INT16 yytype_int16;
#else
typedef short int yytype_int16;
#endif

#ifndef YYSIZE_T
# ifdef __SIZE_TYPE__
#  define YYSIZE_T __SIZE_TYPE__
# elif defined size_t
#  define YYSIZE_T size_t
# elif ! defined YYSIZE_T
#  include <stddef.h> /* INFRINGES ON USER NAME SPACE */
#  define YYSIZE_T size_t
# else
#  define YYSIZE_T unsigned int
# endif
#endif

#define YYSIZE_MAXIMUM ((YYSIZE_T) -1)

#ifndef YY_
# if defined YYENABLE_NLS && YYENABLE_NLS
#  if ENABLE_NLS
#   include <libintl.h> /* INFRINGES ON USER NAME SPACE */
#   define YY_(Msgid) dgettext ("bison-runtime", Msgid)
#  endif
# endif
# ifndef YY_
#  define YY_(Msgid) Msgid
# endif
#endif

#ifndef YY_ATTRIBUTE
# if (defined __GNUC__                                               \
      && (2 < __GNUC__ || (__GNUC__ == 2 && 96 <= __GNUC_MINOR__)))  \
     || defined __SUNPRO_C && 0x5110 <= __SUNPRO_C
#  define YY_ATTRIBUTE(Spec) __attribute__(Spec)
# else
#  define YY_ATTRIBUTE(Spec) /* empty */
# endif
#endif

#ifndef YY_ATTRIBUTE_PURE
# define YY_ATTRIBUTE_PURE   YY_ATTRIBUTE ((__pure__))
#endif

#ifndef YY_ATTRIBUTE_UNUSED
# define YY_ATTRIBUTE_UNUSED YY_ATTRIBUTE ((__unused__))
#endif

#if !defined _Noreturn \
     && (!defined __STDC_VERSION__ || __STDC_VERSION__ < 201112)
# if defined _MSC_VER && 1200 <= _MSC_VER
#  define _Noreturn __declspec (noreturn)
# else
#  define _Noreturn YY_ATTRIBUTE ((__noreturn__))
# endif
#endif

/* Suppress unused-variable warnings by "using" E.  */
#if ! defined lint || defined __GNUC__
# define YYUSE(E) ((void) (E))
#else
# define YYUSE(E) /* empty */
#endif

#if defined __GNUC__ && 407 <= __GNUC__ * 100 + __GNUC_MINOR__
/* Suppress an incorrect diagnostic about yylval being uninitialized.  */
# define YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN \
    _Pragma ("GCC diagnostic push") \
    _Pragma ("GCC diagnostic ignored \"-Wuninitialized\"")\
    _Pragma ("GCC diagnostic ignored \"-Wmaybe-uninitialized\"")
# define YY_IGNORE_MAYBE_UNINITIALIZED_END \
    _Pragma ("GCC diagnostic pop")
#else
# define YY_INITIAL_VALUE(Value) Value
#endif
#ifndef YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
# define YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
# define YY_IGNORE_MAYBE_UNINITIALIZED_END
#endif
#ifndef YY_INITIAL_VALUE
# define YY_INITIAL_VALUE(Value) /* Nothing. */
#endif


#if ! defined yyoverflow || YYERROR_VERBOSE

/* The parser invokes alloca or malloc; define the necessary symbols.  */

# ifdef YYSTACK_USE_ALLOCA
#  if YYSTACK_USE_ALLOCA
#   ifdef __GNUC__
#    define YYSTACK_ALLOC __builtin_alloca
#   elif defined __BUILTIN_VA_ARG_INCR
#    include <alloca.h> /* INFRINGES ON USER NAME SPACE */
#   elif defined _AIX
#    define YYSTACK_ALLOC __alloca
#   elif defined _MSC_VER
#    include <malloc.h> /* INFRINGES ON USER NAME SPACE */
#    define alloca _alloca
#   else
#    define YYSTACK_ALLOC alloca
#    if ! defined _ALLOCA_H && ! defined EXIT_SUCCESS
#     include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
      /* Use EXIT_SUCCESS as a witness for stdlib.h.  */
#     ifndef EXIT_SUCCESS
#      define EXIT_SUCCESS 0
#     endif
#    endif
#   endif
#  endif
# endif

# ifdef YYSTACK_ALLOC
   /* Pacify GCC's 'empty if-body' warning.  */
#  define YYSTACK_FREE(Ptr) do { /* empty */; } while (0)
#  ifndef YYSTACK_ALLOC_MAXIMUM
    /* The OS might guarantee only one guard page at the bottom of the stack,
       and a page size can be as small as 4096 bytes.  So we cannot safely
       invoke alloca (N) if N exceeds 4096.  Use a slightly smaller number
       to allow for a few compiler-allocated temporary stack slots.  */
#   define YYSTACK_ALLOC_MAXIMUM 4032 /* reasonable circa 2006 */
#  endif
# else
#  define YYSTACK_ALLOC YYMALLOC
#  define YYSTACK_FREE YYFREE
#  ifndef YYSTACK_ALLOC_MAXIMUM
#   define YYSTACK_ALLOC_MAXIMUM YYSIZE_MAXIMUM
#  endif
#  if (defined __cplusplus && ! defined EXIT_SUCCESS \
       && ! ((defined YYMALLOC || defined malloc) \
             && (defined YYFREE || defined free)))
#   include <stdlib.h> /* INFRINGES ON USER NAME SPACE */
#   ifndef EXIT_SUCCESS
#    define EXIT_SUCCESS 0
#   endif
#  endif
#  ifndef YYMALLOC
#   define YYMALLOC malloc
#   if ! defined malloc && ! defined EXIT_SUCCESS
void *malloc (YYSIZE_T); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
#  ifndef YYFREE
#   define YYFREE free
#   if ! defined free && ! defined EXIT_SUCCESS
void free (void *); /* INFRINGES ON USER NAME SPACE */
#   endif
#  endif
# endif
#endif /* ! defined yyoverflow || YYERROR_VERBOSE */


#if (! defined yyoverflow \
     && (! defined __cplusplus \
         || (defined YYSTYPE_IS_TRIVIAL && YYSTYPE_IS_TRIVIAL)))

/* A type that is properly aligned for any stack member.  */
union yyalloc
{
  yytype_int16 yyss_alloc;
  YYSTYPE yyvs_alloc;
};

/* The size of the maximum gap between one aligned stack and the next.  */
# define YYSTACK_GAP_MAXIMUM (sizeof (union yyalloc) - 1)

/* The size of an array large to enough to hold all stacks, each with
   N elements.  */
# define YYSTACK_BYTES(N) \
     ((N) * (sizeof (yytype_int16) + sizeof (YYSTYPE)) \
      + YYSTACK_GAP_MAXIMUM)

# define YYCOPY_NEEDED 1

/* Relocate STACK from its old location to the new one.  The
   local variables YYSIZE and YYSTACKSIZE give the old and new number of
   elements in the stack, and YYPTR gives the new location of the
   stack.  Advance YYPTR to a properly aligned location for the next
   stack.  */
# define YYSTACK_RELOCATE(Stack_alloc, Stack)                           \
    do                                                                  \
      {                                                                 \
        YYSIZE_T yynewbytes;                                            \
        YYCOPY (&yyptr->Stack_alloc, Stack, yysize);                    \
        Stack = &yyptr->Stack_alloc;                                    \
        yynewbytes = yystacksize * sizeof (*Stack) + YYSTACK_GAP_MAXIMUM; \
        yyptr += yynewbytes / sizeof (*yyptr);                          \
      }                                                                 \
    while (0)

#endif

#if defined YYCOPY_NEEDED && YYCOPY_NEEDED
/* Copy COUNT objects from SRC to DST.  The source and destination do
   not overlap.  */
# ifndef YYCOPY
#  if defined __GNUC__ && 1 < __GNUC__
#   define YYCOPY(Dst, Src, Count) \
      __builtin_memcpy (Dst, Src, (Count) * sizeof (*(Src)))
#  else
#   define YYCOPY(Dst, Src, Count)              \
      do                                        \
        {                                       \
          YYSIZE_T yyi;                         \
          for (yyi = 0; yyi < (Count); yyi++)   \
            (Dst)[yyi] = (Src)[yyi];            \
        }                                       \
      while (0)
#  endif
# endif
#endif /* !YYCOPY_NEEDED */

/* YYFINAL -- State number of the termination state.  */
#define YYFINAL  5
/* YYLAST -- Last index in YYTABLE.  */
#define YYLAST   330

/* YYNTOKENS -- Number of terminals.  */
#define YYNTOKENS  49
/* YYNNTS -- Number of nonterminals.  */
#define YYNNTS  25
/* YYNRULES -- Number of rules.  */
#define YYNRULES  93
/* YYNSTATES -- Number of states.  */
#define YYNSTATES  224

/* YYTRANSLATE[YYX] -- Symbol number corresponding to YYX as returned
   by yylex, with out-of-bounds checking.  */
#define YYUNDEFTOK  2
#define YYMAXUTOK   303

#define YYTRANSLATE(YYX)                                                \
  ((unsigned int) (YYX) <= YYMAXUTOK ? yytranslate[YYX] : YYUNDEFTOK)

/* YYTRANSLATE[TOKEN-NUM] -- Symbol number corresponding to TOKEN-NUM
   as returned by yylex, without out-of-bounds checking.  */
static const yytype_uint8 yytranslate[] =
{
       0,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     2,     1,     2,     3,     4,
       5,     6,     7,     8,     9,    10,    11,    12,    13,    14,
      15,    16,    17,    18,    19,    20,    21,    22,    23,    24,
      25,    26,    27,    28,    29,    30,    31,    32,    33,    34,
      35,    36,    37,    38,    39,    40,    41,    42,    43,    44,
      45,    46,    47,    48
};

#if YYDEBUG
  /* YYRLINE[YYN] -- Source line where rule number YYN was defined.  */
static const yytype_uint8 yyrline[] =
{
       0,   107,   107,   108,   112,   111,   115,   117,   119,   121,
     123,   125,   127,   129,   130,   131,   132,   133,   135,   136,
     137,   138,   139,   140,   141,   142,   143,   144,   145,   146,
     149,   150,   153,   154,   155,   156,   157,   158,   159,   160,
     161,   162,   163,   164,   165,   166,   167,   168,   169,   170,
     171,   172,   173,   174,   177,   178,   181,   182,   185,   185,
     185,   187,   188,   189,   190,   191,   193,   193,   200,   208,
     216,   217,   218,   219,   220,   221,   222,   223,   224,   225,
     226,   227,   228,   229,   233,   232,   236,   237,   238,   245,
     246,   247,   248,   255
};
#endif

#if YYDEBUG || YYERROR_VERBOSE || 0
/* YYTNAME[SYMBOL-NUM] -- String name of the symbol SYMBOL-NUM.
   First, the terminals, then, starting at YYNTOKENS, nonterminals.  */
static const char *const yytname[] =
{
  "$end", "error", "$undefined", "STATE_CFG", "SHARED", "CONFIGURABLE",
  "CHILD_TYPE", "UNSIGNED", "SIGNED", "DOUBLE", "SHORT", "FLOAT", "VOID",
  "CHAR", "LONG_LONG", "LONG_INT", "LONG", "INT", "IDENTIFIER", "CONSTANT",
  "STRING_LITERAL", "LCURL", "RCURL", "LANGLE", "RANGLE", "SEMI", "EQ",
  "COMMA", "STAR", "AMPR", "COLONS", "T1", "T2", "T1_TPL", "T2_TPL",
  "TYPEID", "FILENAME", "ALIAS", "EMPTY", "DEFAULT_VALUE", "DOC_STR",
  "TYPE", "UNSERIALIZE_FCT", "SERIALIZE_FCT", "VISUALZIED", "STATISTIC",
  "MIN_COUNT", "MAX_COUNT", "IS_ALIASED", "$accept", "state_configs",
  "state_config", "$@1", "class_name", "extends_name", "class_tpl",
  "extends_tpl", "file_name", "type_id", "alias", "state_config_inner",
  "identifier", "tplparams", "type", "scopped_identifier", "scopped_type",
  "value", "var_prefix", "variable", "$@2", "variable_inner", "child_type",
  "$@3", "child_type_inner", YY_NULLPTR
};
#endif

# ifdef YYPRINT
/* YYTOKNUM[NUM] -- (External) token number corresponding to the
   (internal) symbol number NUM (which must be that of a token).  */
static const yytype_uint16 yytoknum[] =
{
       0,   256,   257,   258,   259,   260,   261,   262,   263,   264,
     265,   266,   267,   268,   269,   270,   271,   272,   273,   274,
     275,   276,   277,   278,   279,   280,   281,   282,   283,   284,
     285,   286,   287,   288,   289,   290,   291,   292,   293,   294,
     295,   296,   297,   298,   299,   300,   301,   302,   303
};
# endif

#define YYPACT_NINF -138

#define yypact_value_is_default(Yystate) \
  (!!((Yystate) == (-138)))

#define YYTABLE_NINF -1

#define yytable_value_is_error(Yytable_value) \
  0

  /* YYPACT[STATE-NUM] -- Index in YYTABLE of the portion describing
     STATE-NUM.  */
static const yytype_int16 yypact[] =
{
       3,   -14,    14,     8,   -15,  -138,     3,    18,    13,  -138,
      26,    34,    47,    42,    49,    55,    53,  -138,    63,   100,
      95,    87,  -138,   112,   118,   113,   108,  -138,   127,   159,
     140,   144,  -138,   156,   162,   157,  -138,  -138,   165,   164,
      86,  -138,   166,   160,   146,   172,     5,   249,  -138,  -138,
    -138,  -138,  -138,  -138,  -138,  -138,  -138,  -138,   171,  -138,
    -138,  -138,  -138,  -138,  -138,  -138,  -138,   101,    30,  -138,
      36,     4,   173,   180,  -138,   202,    36,   202,    36,   186,
    -138,  -138,  -138,  -138,  -138,  -138,  -138,  -138,  -138,  -138,
    -138,  -138,  -138,  -138,   196,   197,   186,  -138,  -138,   202,
      30,  -138,     9,  -138,  -138,    36,    30,    36,    30,    30,
      41,  -138,  -138,   199,    -4,  -138,   228,  -138,  -138,   167,
    -138,    30,    30,   206,   186,  -138,   186,   204,   205,   210,
     211,   212,   214,   220,   213,   218,    30,  -138,     9,   227,
     233,   186,   186,   241,   242,  -138,   248,   250,   251,   252,
     253,   254,   255,  -138,   257,   258,   259,    43,   105,   260,
     261,     9,   267,   270,   186,   186,   256,   271,   -36,  -138,
    -138,  -138,  -138,  -138,  -138,  -138,   264,   266,   268,   106,
     121,   269,   272,   273,   274,   275,   276,    -9,  -138,  -138,
    -138,  -138,  -138,  -138,  -138,   278,   277,   284,     9,  -138,
     279,   280,   281,   282,   285,   286,   287,   288,   289,   295,
     296,     9,  -138,  -138,  -138,  -138,   291,   292,   293,   294,
    -138,  -138,  -138,  -138
};

  /* YYDEFACT[STATE-NUM] -- Default reduction number in state STATE-NUM.
     Performed when YYTABLE does not specify something else to do.  Zero
     means the default is an error.  */
static const yytype_uint8 yydefact[] =
{
       0,     0,     0,     0,     0,     1,     3,     0,     0,     2,
       0,     0,     0,     0,     0,     0,     0,     6,     0,     0,
       0,     0,     7,     0,     0,     0,     0,     8,     0,     0,
       0,     0,     9,     0,     0,     0,     4,    11,     0,     0,
       0,    12,     0,     0,     0,     0,     0,     0,    48,    49,
      50,    51,    52,    45,    46,    47,    53,    18,     0,    19,
      20,    21,    22,    23,    24,    25,    26,     0,    32,    57,
       0,    69,     0,     0,    10,     0,     0,     0,     0,     0,
      36,    37,    33,    34,    35,    38,    42,    43,    39,    40,
      41,    44,    15,     5,     0,     0,     0,    28,    29,     0,
      65,    66,     0,    13,    14,     0,    64,     0,    63,    55,
       0,    16,    17,     0,    31,    56,     0,    59,    60,    58,
      68,    62,    61,     0,     0,    27,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,    54,    30,     0,     0,
       0,     0,     0,     0,     0,    67,     0,     0,     0,     0,
       0,     0,     0,    84,     0,     0,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,     0,    70,
      71,    72,    73,    74,    75,    76,     0,     0,     0,     0,
       0,     0,     0,     0,     0,     0,     0,     0,    77,    78,
      79,    80,    81,    82,    83,     0,     0,     0,     0,    85,
       0,     0,     0,     0,     0,     0,     0,     0,     0,     0,
       0,     0,    89,    86,    87,    88,     0,     0,     0,     0,
      93,    90,    91,    92
};

  /* YYPGOTO[NTERM-NUM].  */
static const yytype_int16 yypgoto[] =
{
    -138,   298,  -138,  -138,  -138,  -138,  -138,  -138,  -138,  -138,
    -138,  -138,   -35,   169,   193,   -79,   -41,  -137,  -138,   262,
    -138,  -138,   263,  -138,  -138
};

  /* YYDEFGOTO[NTERM-NUM].  */
static const yytype_int16 yydefgoto[] =
{
      -1,     2,     3,    40,     8,    12,    16,    21,    36,    26,
      31,    67,   109,   113,    69,   119,    70,   120,    71,    72,
     116,   134,    73,   168,   187
};

  /* YYTABLE[YYPACT[STATE-NUM]] -- What to do in state STATE-NUM.  If
     positive, shift that token.  If negative, reduce the rule whose
     number is the opposite.  If YYTABLE_NINF, syntax error.  */
static const yytype_uint8 yytable[] =
{
     110,   154,    76,    78,   183,    68,     1,     4,    68,    68,
     184,   185,   186,   199,     5,    80,     7,   114,    81,    82,
      83,    84,    85,   126,   176,   101,   124,    57,   117,   118,
     102,   200,    68,     6,   105,   100,   107,   201,   202,   203,
      68,   106,    68,   108,    10,    11,    13,   114,    59,    60,
      61,    62,    63,    96,    57,    64,    65,    66,    97,    98,
      14,   207,   157,   158,    68,   123,    99,    17,   172,    18,
     121,   124,   122,   124,   219,    59,    60,    61,    62,    63,
      15,    19,    64,    65,    66,   179,   180,    20,    22,   136,
      43,    44,    45,    46,    47,    48,    49,    50,    51,    52,
      53,    54,    55,    56,    57,    43,    44,    45,    46,    47,
      48,    49,    50,    51,    52,    53,    54,    55,    56,    57,
      23,    24,    25,    93,    58,    59,    60,    61,    62,    63,
     173,   191,    64,    65,    66,   124,   124,    27,    28,    29,
      59,    60,    61,    62,    63,    30,   192,    64,    65,    66,
      77,   124,    32,    46,    47,    48,    49,    50,    51,    52,
      53,    54,    55,    56,    57,    75,    34,    46,    47,    48,
      49,    50,    51,    52,    53,    54,    55,    56,    57,    33,
      35,    37,    38,    39,    42,    59,    60,    61,    62,    63,
      41,    74,    64,    65,    66,    79,    92,   124,   103,    59,
      60,    61,    62,    63,    57,   104,    64,    65,    66,    46,
      47,    48,    49,    50,    51,    52,    53,    54,    55,    56,
      57,   111,   112,   125,   135,    59,    60,    61,    62,    63,
     138,   139,    64,    65,    66,   145,   140,   141,   142,   153,
     143,    59,    60,    61,    62,    63,   144,   155,    64,    65,
      66,   156,   146,   147,   148,   149,   150,   151,   152,    86,
     159,   160,    87,    88,    89,    90,    91,   127,   128,   129,
     130,   131,   132,   133,   161,   181,   162,   163,   164,   165,
     166,   167,   169,   170,   171,   174,   175,   177,   178,   188,
     182,   189,   115,   190,   193,   137,   205,   194,   204,   195,
     196,   197,   198,   206,     9,   208,   209,   210,   211,   216,
     212,   213,   214,   215,   217,   218,   220,   221,   222,   223,
       0,     0,     0,     0,     0,     0,     0,     0,     0,    94,
      95
};

static const yytype_int16 yycheck[] =
{
      79,   138,    43,    44,    40,    40,     3,    21,    43,    44,
      46,    47,    48,    22,     0,    10,    31,    96,    13,    14,
      15,    16,    17,    27,   161,    21,    30,    18,    19,    20,
      26,    40,    67,    25,    75,    70,    77,    46,    47,    48,
      75,    76,    77,    78,    26,    32,    20,   126,    39,    40,
      41,    42,    43,    23,    18,    46,    47,    48,    28,    29,
      26,   198,   141,   142,    99,    24,    30,    25,    25,    20,
     105,    30,   107,    30,   211,    39,    40,    41,    42,    43,
      33,    26,    46,    47,    48,   164,   165,    34,    25,   124,
       4,     5,     6,     7,     8,     9,    10,    11,    12,    13,
      14,    15,    16,    17,    18,     4,     5,     6,     7,     8,
       9,    10,    11,    12,    13,    14,    15,    16,    17,    18,
      20,    26,    35,    22,    38,    39,    40,    41,    42,    43,
      25,    25,    46,    47,    48,    30,    30,    25,    20,    26,
      39,    40,    41,    42,    43,    37,    25,    46,    47,    48,
       4,    30,    25,     7,     8,     9,    10,    11,    12,    13,
      14,    15,    16,    17,    18,     5,    26,     7,     8,     9,
      10,    11,    12,    13,    14,    15,    16,    17,    18,    20,
      36,    25,    20,    26,    20,    39,    40,    41,    42,    43,
      25,    25,    46,    47,    48,    23,    25,    30,    25,    39,
      40,    41,    42,    43,    18,    25,    46,    47,    48,     7,
       8,     9,    10,    11,    12,    13,    14,    15,    16,    17,
      18,    25,    25,    24,    18,    39,    40,    41,    42,    43,
      26,    26,    46,    47,    48,    22,    26,    26,    26,    21,
      26,    39,    40,    41,    42,    43,    26,    20,    46,    47,
      48,    18,    39,    40,    41,    42,    43,    44,    45,    10,
      19,    19,    13,    14,    15,    16,    17,    39,    40,    41,
      42,    43,    44,    45,    26,    19,    26,    26,    26,    26,
      26,    26,    25,    25,    25,    25,    25,    20,    18,    25,
      19,    25,    99,    25,    25,   126,    19,    25,    20,    26,
      26,    26,    26,    19,     6,    26,    26,    26,    26,    20,
      25,    25,    25,    25,    19,    19,    25,    25,    25,    25,
      -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    -1,    67,
      67
};

  /* YYSTOS[STATE-NUM] -- The (internal number of the) accessing
     symbol of state STATE-NUM.  */
static const yytype_uint8 yystos[] =
{
       0,     3,    50,    51,    21,     0,    25,    31,    53,    50,
      26,    32,    54,    20,    26,    33,    55,    25,    20,    26,
      34,    56,    25,    20,    26,    35,    58,    25,    20,    26,
      37,    59,    25,    20,    26,    36,    57,    25,    20,    26,
      52,    25,    20,     4,     5,     6,     7,     8,     9,    10,
      11,    12,    13,    14,    15,    16,    17,    18,    38,    39,
      40,    41,    42,    43,    46,    47,    48,    60,    61,    63,
      65,    67,    68,    71,    25,     5,    65,     4,    65,    23,
      10,    13,    14,    15,    16,    17,    10,    13,    14,    15,
      16,    17,    25,    22,    68,    71,    23,    28,    29,    30,
      61,    21,    26,    25,    25,    65,    61,    65,    61,    61,
      64,    25,    25,    62,    64,    63,    69,    19,    20,    64,
      66,    61,    61,    24,    30,    24,    27,    39,    40,    41,
      42,    43,    44,    45,    70,    18,    61,    62,    26,    26,
      26,    26,    26,    26,    26,    22,    39,    40,    41,    42,
      43,    44,    45,    21,    66,    20,    18,    64,    64,    19,
      19,    26,    26,    26,    26,    26,    26,    26,    72,    25,
      25,    25,    25,    25,    25,    25,    66,    20,    18,    64,
      64,    19,    19,    40,    46,    47,    48,    73,    25,    25,
      25,    25,    25,    25,    25,    26,    26,    26,    26,    22,
      40,    46,    47,    48,    20,    19,    19,    66,    26,    26,
      26,    26,    25,    25,    25,    25,    20,    19,    19,    66,
      25,    25,    25,    25
};

  /* YYR1[YYN] -- Symbol number of symbol that rule YYN derives.  */
static const yytype_uint8 yyr1[] =
{
       0,    49,    50,    50,    52,    51,    53,    54,    55,    56,
      57,    58,    59,    60,    60,    60,    60,    60,    61,    61,
      61,    61,    61,    61,    61,    61,    61,    61,    61,    61,
      62,    62,    63,    63,    63,    63,    63,    63,    63,    63,
      63,    63,    63,    63,    63,    63,    63,    63,    63,    63,
      63,    63,    63,    63,    64,    64,    65,    65,    66,    66,
      66,    67,    67,    67,    67,    67,    69,    68,    68,    68,
      70,    70,    70,    70,    70,    70,    70,    70,    70,    70,
      70,    70,    70,    70,    72,    71,    73,    73,    73,    73,
      73,    73,    73,    73
};

  /* YYR2[YYN] -- Number of symbols on the right hand side of rule YYN.  */
static const yytype_uint8 yyr2[] =
{
       0,     2,     3,     2,     0,    12,     4,     4,     4,     4,
       4,     4,     4,     2,     2,     2,     3,     3,     1,     1,
       1,     1,     1,     1,     1,     1,     1,     4,     2,     2,
       3,     1,     1,     2,     2,     2,     2,     2,     2,     2,
       2,     2,     2,     2,     2,     1,     1,     1,     1,     1,
       1,     1,     1,     1,     3,     1,     3,     1,     1,     1,
       1,     4,     4,     3,     3,     2,     0,     5,     3,     1,
       4,     4,     4,     4,     4,     4,     4,     5,     5,     5,
       5,     5,     5,     5,     0,     9,     4,     4,     4,     4,
       5,     5,     5,     5
};


#define yyerrok         (yyerrstatus = 0)
#define yyclearin       (yychar = YYEMPTY)
#define YYEMPTY         (-2)
#define YYEOF           0

#define YYACCEPT        goto yyacceptlab
#define YYABORT         goto yyabortlab
#define YYERROR         goto yyerrorlab


#define YYRECOVERING()  (!!yyerrstatus)

#define YYBACKUP(Token, Value)                                  \
do                                                              \
  if (yychar == YYEMPTY)                                        \
    {                                                           \
      yychar = (Token);                                         \
      yylval = (Value);                                         \
      YYPOPSTACK (yylen);                                       \
      yystate = *yyssp;                                         \
      goto yybackup;                                            \
    }                                                           \
  else                                                          \
    {                                                           \
      yyerror (YY_("syntax error: cannot back up")); \
      YYERROR;                                                  \
    }                                                           \
while (0)

/* Error token number */
#define YYTERROR        1
#define YYERRCODE       256



/* Enable debugging if requested.  */
#if YYDEBUG

# ifndef YYFPRINTF
#  include <stdio.h> /* INFRINGES ON USER NAME SPACE */
#  define YYFPRINTF fprintf
# endif

# define YYDPRINTF(Args)                        \
do {                                            \
  if (yydebug)                                  \
    YYFPRINTF Args;                             \
} while (0)

/* This macro is provided for backward compatibility. */
#ifndef YY_LOCATION_PRINT
# define YY_LOCATION_PRINT(File, Loc) ((void) 0)
#endif


# define YY_SYMBOL_PRINT(Title, Type, Value, Location)                    \
do {                                                                      \
  if (yydebug)                                                            \
    {                                                                     \
      YYFPRINTF (stderr, "%s ", Title);                                   \
      yy_symbol_print (stderr,                                            \
                  Type, Value); \
      YYFPRINTF (stderr, "\n");                                           \
    }                                                                     \
} while (0)


/*----------------------------------------.
| Print this symbol's value on YYOUTPUT.  |
`----------------------------------------*/

static void
yy_symbol_value_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
{
  FILE *yyo = yyoutput;
  YYUSE (yyo);
  if (!yyvaluep)
    return;
# ifdef YYPRINT
  if (yytype < YYNTOKENS)
    YYPRINT (yyoutput, yytoknum[yytype], *yyvaluep);
# endif
  YYUSE (yytype);
}


/*--------------------------------.
| Print this symbol on YYOUTPUT.  |
`--------------------------------*/

static void
yy_symbol_print (FILE *yyoutput, int yytype, YYSTYPE const * const yyvaluep)
{
  YYFPRINTF (yyoutput, "%s %s (",
             yytype < YYNTOKENS ? "token" : "nterm", yytname[yytype]);

  yy_symbol_value_print (yyoutput, yytype, yyvaluep);
  YYFPRINTF (yyoutput, ")");
}

/*------------------------------------------------------------------.
| yy_stack_print -- Print the state stack from its BOTTOM up to its |
| TOP (included).                                                   |
`------------------------------------------------------------------*/

static void
yy_stack_print (yytype_int16 *yybottom, yytype_int16 *yytop)
{
  YYFPRINTF (stderr, "Stack now");
  for (; yybottom <= yytop; yybottom++)
    {
      int yybot = *yybottom;
      YYFPRINTF (stderr, " %d", yybot);
    }
  YYFPRINTF (stderr, "\n");
}

# define YY_STACK_PRINT(Bottom, Top)                            \
do {                                                            \
  if (yydebug)                                                  \
    yy_stack_print ((Bottom), (Top));                           \
} while (0)


/*------------------------------------------------.
| Report that the YYRULE is going to be reduced.  |
`------------------------------------------------*/

static void
yy_reduce_print (yytype_int16 *yyssp, YYSTYPE *yyvsp, int yyrule)
{
  unsigned long int yylno = yyrline[yyrule];
  int yynrhs = yyr2[yyrule];
  int yyi;
  YYFPRINTF (stderr, "Reducing stack by rule %d (line %lu):\n",
             yyrule - 1, yylno);
  /* The symbols being reduced.  */
  for (yyi = 0; yyi < yynrhs; yyi++)
    {
      YYFPRINTF (stderr, "   $%d = ", yyi + 1);
      yy_symbol_print (stderr,
                       yystos[yyssp[yyi + 1 - yynrhs]],
                       &(yyvsp[(yyi + 1) - (yynrhs)])
                                              );
      YYFPRINTF (stderr, "\n");
    }
}

# define YY_REDUCE_PRINT(Rule)          \
do {                                    \
  if (yydebug)                          \
    yy_reduce_print (yyssp, yyvsp, Rule); \
} while (0)

/* Nonzero means print parse trace.  It is left uninitialized so that
   multiple parsers can coexist.  */
int yydebug;
#else /* !YYDEBUG */
# define YYDPRINTF(Args)
# define YY_SYMBOL_PRINT(Title, Type, Value, Location)
# define YY_STACK_PRINT(Bottom, Top)
# define YY_REDUCE_PRINT(Rule)
#endif /* !YYDEBUG */


/* YYINITDEPTH -- initial size of the parser's stacks.  */
#ifndef YYINITDEPTH
# define YYINITDEPTH 200
#endif

/* YYMAXDEPTH -- maximum size the stacks can grow to (effective only
   if the built-in stack extension method is used).

   Do not make this value too large; the results are undefined if
   YYSTACK_ALLOC_MAXIMUM < YYSTACK_BYTES (YYMAXDEPTH)
   evaluated with infinite-precision integer arithmetic.  */

#ifndef YYMAXDEPTH
# define YYMAXDEPTH 10000
#endif


#if YYERROR_VERBOSE

# ifndef yystrlen
#  if defined __GLIBC__ && defined _STRING_H
#   define yystrlen strlen
#  else
/* Return the length of YYSTR.  */
static YYSIZE_T
yystrlen (const char *yystr)
{
  YYSIZE_T yylen;
  for (yylen = 0; yystr[yylen]; yylen++)
    continue;
  return yylen;
}
#  endif
# endif

# ifndef yystpcpy
#  if defined __GLIBC__ && defined _STRING_H && defined _GNU_SOURCE
#   define yystpcpy stpcpy
#  else
/* Copy YYSRC to YYDEST, returning the address of the terminating '\0' in
   YYDEST.  */
static char *
yystpcpy (char *yydest, const char *yysrc)
{
  char *yyd = yydest;
  const char *yys = yysrc;

  while ((*yyd++ = *yys++) != '\0')
    continue;

  return yyd - 1;
}
#  endif
# endif

# ifndef yytnamerr
/* Copy to YYRES the contents of YYSTR after stripping away unnecessary
   quotes and backslashes, so that it's suitable for yyerror.  The
   heuristic is that double-quoting is unnecessary unless the string
   contains an apostrophe, a comma, or backslash (other than
   backslash-backslash).  YYSTR is taken from yytname.  If YYRES is
   null, do not copy; instead, return the length of what the result
   would have been.  */
static YYSIZE_T
yytnamerr (char *yyres, const char *yystr)
{
  if (*yystr == '"')
    {
      YYSIZE_T yyn = 0;
      char const *yyp = yystr;

      for (;;)
        switch (*++yyp)
          {
          case '\'':
          case ',':
            goto do_not_strip_quotes;

          case '\\':
            if (*++yyp != '\\')
              goto do_not_strip_quotes;
            /* Fall through.  */
          default:
            if (yyres)
              yyres[yyn] = *yyp;
            yyn++;
            break;

          case '"':
            if (yyres)
              yyres[yyn] = '\0';
            return yyn;
          }
    do_not_strip_quotes: ;
    }

  if (! yyres)
    return yystrlen (yystr);

  return yystpcpy (yyres, yystr) - yyres;
}
# endif

/* Copy into *YYMSG, which is of size *YYMSG_ALLOC, an error message
   about the unexpected token YYTOKEN for the state stack whose top is
   YYSSP.

   Return 0 if *YYMSG was successfully written.  Return 1 if *YYMSG is
   not large enough to hold the message.  In that case, also set
   *YYMSG_ALLOC to the required number of bytes.  Return 2 if the
   required number of bytes is too large to store.  */
static int
yysyntax_error (YYSIZE_T *yymsg_alloc, char **yymsg,
                yytype_int16 *yyssp, int yytoken)
{
  YYSIZE_T yysize0 = yytnamerr (YY_NULLPTR, yytname[yytoken]);
  YYSIZE_T yysize = yysize0;
  enum { YYERROR_VERBOSE_ARGS_MAXIMUM = 5 };
  /* Internationalized format string. */
  const char *yyformat = YY_NULLPTR;
  /* Arguments of yyformat. */
  char const *yyarg[YYERROR_VERBOSE_ARGS_MAXIMUM];
  /* Number of reported tokens (one for the "unexpected", one per
     "expected"). */
  int yycount = 0;

  /* There are many possibilities here to consider:
     - If this state is a consistent state with a default action, then
       the only way this function was invoked is if the default action
       is an error action.  In that case, don't check for expected
       tokens because there are none.
     - The only way there can be no lookahead present (in yychar) is if
       this state is a consistent state with a default action.  Thus,
       detecting the absence of a lookahead is sufficient to determine
       that there is no unexpected or expected token to report.  In that
       case, just report a simple "syntax error".
     - Don't assume there isn't a lookahead just because this state is a
       consistent state with a default action.  There might have been a
       previous inconsistent state, consistent state with a non-default
       action, or user semantic action that manipulated yychar.
     - Of course, the expected token list depends on states to have
       correct lookahead information, and it depends on the parser not
       to perform extra reductions after fetching a lookahead from the
       scanner and before detecting a syntax error.  Thus, state merging
       (from LALR or IELR) and default reductions corrupt the expected
       token list.  However, the list is correct for canonical LR with
       one exception: it will still contain any token that will not be
       accepted due to an error action in a later state.
  */
  if (yytoken != YYEMPTY)
    {
      int yyn = yypact[*yyssp];
      yyarg[yycount++] = yytname[yytoken];
      if (!yypact_value_is_default (yyn))
        {
          /* Start YYX at -YYN if negative to avoid negative indexes in
             YYCHECK.  In other words, skip the first -YYN actions for
             this state because they are default actions.  */
          int yyxbegin = yyn < 0 ? -yyn : 0;
          /* Stay within bounds of both yycheck and yytname.  */
          int yychecklim = YYLAST - yyn + 1;
          int yyxend = yychecklim < YYNTOKENS ? yychecklim : YYNTOKENS;
          int yyx;

          for (yyx = yyxbegin; yyx < yyxend; ++yyx)
            if (yycheck[yyx + yyn] == yyx && yyx != YYTERROR
                && !yytable_value_is_error (yytable[yyx + yyn]))
              {
                if (yycount == YYERROR_VERBOSE_ARGS_MAXIMUM)
                  {
                    yycount = 1;
                    yysize = yysize0;
                    break;
                  }
                yyarg[yycount++] = yytname[yyx];
                {
                  YYSIZE_T yysize1 = yysize + yytnamerr (YY_NULLPTR, yytname[yyx]);
                  if (! (yysize <= yysize1
                         && yysize1 <= YYSTACK_ALLOC_MAXIMUM))
                    return 2;
                  yysize = yysize1;
                }
              }
        }
    }

  switch (yycount)
    {
# define YYCASE_(N, S)                      \
      case N:                               \
        yyformat = S;                       \
      break
      YYCASE_(0, YY_("syntax error"));
      YYCASE_(1, YY_("syntax error, unexpected %s"));
      YYCASE_(2, YY_("syntax error, unexpected %s, expecting %s"));
      YYCASE_(3, YY_("syntax error, unexpected %s, expecting %s or %s"));
      YYCASE_(4, YY_("syntax error, unexpected %s, expecting %s or %s or %s"));
      YYCASE_(5, YY_("syntax error, unexpected %s, expecting %s or %s or %s or %s"));
# undef YYCASE_
    }

  {
    YYSIZE_T yysize1 = yysize + yystrlen (yyformat);
    if (! (yysize <= yysize1 && yysize1 <= YYSTACK_ALLOC_MAXIMUM))
      return 2;
    yysize = yysize1;
  }

  if (*yymsg_alloc < yysize)
    {
      *yymsg_alloc = 2 * yysize;
      if (! (yysize <= *yymsg_alloc
             && *yymsg_alloc <= YYSTACK_ALLOC_MAXIMUM))
        *yymsg_alloc = YYSTACK_ALLOC_MAXIMUM;
      return 1;
    }

  /* Avoid sprintf, as that infringes on the user's name space.
     Don't have undefined behavior even if the translation
     produced a string with the wrong number of "%s"s.  */
  {
    char *yyp = *yymsg;
    int yyi = 0;
    while ((*yyp = *yyformat) != '\0')
      if (*yyp == '%' && yyformat[1] == 's' && yyi < yycount)
        {
          yyp += yytnamerr (yyp, yyarg[yyi++]);
          yyformat += 2;
        }
      else
        {
          yyp++;
          yyformat++;
        }
  }
  return 0;
}
#endif /* YYERROR_VERBOSE */

/*-----------------------------------------------.
| Release the memory associated to this symbol.  |
`-----------------------------------------------*/

static void
yydestruct (const char *yymsg, int yytype, YYSTYPE *yyvaluep)
{
  YYUSE (yyvaluep);
  if (!yymsg)
    yymsg = "Deleting";
  YY_SYMBOL_PRINT (yymsg, yytype, yyvaluep, yylocationp);

  YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
  YYUSE (yytype);
  YY_IGNORE_MAYBE_UNINITIALIZED_END
}




/* The lookahead symbol.  */
int yychar;

/* The semantic value of the lookahead symbol.  */
YYSTYPE yylval;
/* Number of syntax errors so far.  */
int yynerrs;


/*----------.
| yyparse.  |
`----------*/

int
yyparse (void)
{
    int yystate;
    /* Number of tokens to shift before error messages enabled.  */
    int yyerrstatus;

    /* The stacks and their tools:
       'yyss': related to states.
       'yyvs': related to semantic values.

       Refer to the stacks through separate pointers, to allow yyoverflow
       to reallocate them elsewhere.  */

    /* The state stack.  */
    yytype_int16 yyssa[YYINITDEPTH];
    yytype_int16 *yyss;
    yytype_int16 *yyssp;

    /* The semantic value stack.  */
    YYSTYPE yyvsa[YYINITDEPTH];
    YYSTYPE *yyvs;
    YYSTYPE *yyvsp;

    YYSIZE_T yystacksize;

  int yyn;
  int yyresult;
  /* Lookahead token as an internal (translated) token number.  */
  int yytoken = 0;
  /* The variables used to return semantic value and location from the
     action routines.  */
  YYSTYPE yyval;

#if YYERROR_VERBOSE
  /* Buffer for error messages, and its allocated size.  */
  char yymsgbuf[128];
  char *yymsg = yymsgbuf;
  YYSIZE_T yymsg_alloc = sizeof yymsgbuf;
#endif

#define YYPOPSTACK(N)   (yyvsp -= (N), yyssp -= (N))

  /* The number of symbols on the RHS of the reduced rule.
     Keep to zero when no symbol should be popped.  */
  int yylen = 0;

  yyssp = yyss = yyssa;
  yyvsp = yyvs = yyvsa;
  yystacksize = YYINITDEPTH;

  YYDPRINTF ((stderr, "Starting parse\n"));

  yystate = 0;
  yyerrstatus = 0;
  yynerrs = 0;
  yychar = YYEMPTY; /* Cause a token to be read.  */
  goto yysetstate;

/*------------------------------------------------------------.
| yynewstate -- Push a new state, which is found in yystate.  |
`------------------------------------------------------------*/
 yynewstate:
  /* In all cases, when you get here, the value and location stacks
     have just been pushed.  So pushing a state here evens the stacks.  */
  yyssp++;

 yysetstate:
  *yyssp = yystate;

  if (yyss + yystacksize - 1 <= yyssp)
    {
      /* Get the current used size of the three stacks, in elements.  */
      YYSIZE_T yysize = yyssp - yyss + 1;

#ifdef yyoverflow
      {
        /* Give user a chance to reallocate the stack.  Use copies of
           these so that the &'s don't force the real ones into
           memory.  */
        YYSTYPE *yyvs1 = yyvs;
        yytype_int16 *yyss1 = yyss;

        /* Each stack pointer address is followed by the size of the
           data in use in that stack, in bytes.  This used to be a
           conditional around just the two extra args, but that might
           be undefined if yyoverflow is a macro.  */
        yyoverflow (YY_("memory exhausted"),
                    &yyss1, yysize * sizeof (*yyssp),
                    &yyvs1, yysize * sizeof (*yyvsp),
                    &yystacksize);

        yyss = yyss1;
        yyvs = yyvs1;
      }
#else /* no yyoverflow */
# ifndef YYSTACK_RELOCATE
      goto yyexhaustedlab;
# else
      /* Extend the stack our own way.  */
      if (YYMAXDEPTH <= yystacksize)
        goto yyexhaustedlab;
      yystacksize *= 2;
      if (YYMAXDEPTH < yystacksize)
        yystacksize = YYMAXDEPTH;

      {
        yytype_int16 *yyss1 = yyss;
        union yyalloc *yyptr =
          (union yyalloc *) YYSTACK_ALLOC (YYSTACK_BYTES (yystacksize));
        if (! yyptr)
          goto yyexhaustedlab;
        YYSTACK_RELOCATE (yyss_alloc, yyss);
        YYSTACK_RELOCATE (yyvs_alloc, yyvs);
#  undef YYSTACK_RELOCATE
        if (yyss1 != yyssa)
          YYSTACK_FREE (yyss1);
      }
# endif
#endif /* no yyoverflow */

      yyssp = yyss + yysize - 1;
      yyvsp = yyvs + yysize - 1;

      YYDPRINTF ((stderr, "Stack size increased to %lu\n",
                  (unsigned long int) yystacksize));

      if (yyss + yystacksize - 1 <= yyssp)
        YYABORT;
    }

  YYDPRINTF ((stderr, "Entering state %d\n", yystate));

  if (yystate == YYFINAL)
    YYACCEPT;

  goto yybackup;

/*-----------.
| yybackup.  |
`-----------*/
yybackup:

  /* Do appropriate processing given the current state.  Read a
     lookahead token if we need one and don't already have one.  */

  /* First try to decide what to do without reference to lookahead token.  */
  yyn = yypact[yystate];
  if (yypact_value_is_default (yyn))
    goto yydefault;

  /* Not known => get a lookahead token if don't already have one.  */

  /* YYCHAR is either YYEMPTY or YYEOF or a valid lookahead symbol.  */
  if (yychar == YYEMPTY)
    {
      YYDPRINTF ((stderr, "Reading a token: "));
      yychar = yylex ();
    }

  if (yychar <= YYEOF)
    {
      yychar = yytoken = YYEOF;
      YYDPRINTF ((stderr, "Now at end of input.\n"));
    }
  else
    {
      yytoken = YYTRANSLATE (yychar);
      YY_SYMBOL_PRINT ("Next token is", yytoken, &yylval, &yylloc);
    }

  /* If the proper action on seeing token YYTOKEN is to reduce or to
     detect an error, take that action.  */
  yyn += yytoken;
  if (yyn < 0 || YYLAST < yyn || yycheck[yyn] != yytoken)
    goto yydefault;
  yyn = yytable[yyn];
  if (yyn <= 0)
    {
      if (yytable_value_is_error (yyn))
        goto yyerrlab;
      yyn = -yyn;
      goto yyreduce;
    }

  /* Count tokens shifted since error; after three, turn off error
     status.  */
  if (yyerrstatus)
    yyerrstatus--;

  /* Shift the lookahead token.  */
  YY_SYMBOL_PRINT ("Shifting", yytoken, &yylval, &yylloc);

  /* Discard the shifted token.  */
  yychar = YYEMPTY;

  yystate = yyn;
  YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
  *++yyvsp = yylval;
  YY_IGNORE_MAYBE_UNINITIALIZED_END

  goto yynewstate;


/*-----------------------------------------------------------.
| yydefault -- do the default action for the current state.  |
`-----------------------------------------------------------*/
yydefault:
  yyn = yydefact[yystate];
  if (yyn == 0)
    goto yyerrlab;
  goto yyreduce;


/*-----------------------------.
| yyreduce -- Do a reduction.  |
`-----------------------------*/
yyreduce:
  /* yyn is the number of a rule to reduce with.  */
  yylen = yyr2[yyn];

  /* If YYLEN is nonzero, implement the default value of the action:
     '$$ = $1'.

     Otherwise, the following line sets YYVAL to garbage.
     This behavior is undocumented and Bison
     users should not rely upon it.  Assigning to YYVAL
     unconditionally makes the parser a bit smaller, and it avoids a
     GCC warning that YYVAL may be used uninitialized.  */
  yyval = yyvsp[1-yylen];


  YY_REDUCE_PRINT (yyn);
  switch (yyn)
    {
        case 4:
#line 112 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {cur_ctype=addConfigType((yyvsp[-6].str), (yyvsp[-4].str), (yyvsp[-5].str), (yyvsp[-3].str), (yyvsp[-2].str), (yyvsp[-1].str), (yyvsp[0].str)); }
#line 1495 "y.tab.c" /* yacc.c:1646  */
    break;

  case 5:
#line 113 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.ctype_ptr) = getHead(); }
#line 1501 "y.tab.c" /* yacc.c:1646  */
    break;

  case 6:
#line 115 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.str)=(yyvsp[-1].str); }
#line 1507 "y.tab.c" /* yacc.c:1646  */
    break;

  case 7:
#line 117 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str)=(yyvsp[-1].str); }
#line 1513 "y.tab.c" /* yacc.c:1646  */
    break;

  case 8:
#line 119 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.str)=(yyvsp[-1].str); }
#line 1519 "y.tab.c" /* yacc.c:1646  */
    break;

  case 9:
#line 121 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str)=(yyvsp[-1].str); }
#line 1525 "y.tab.c" /* yacc.c:1646  */
    break;

  case 10:
#line 123 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.str)=(yyvsp[-1].str); }
#line 1531 "y.tab.c" /* yacc.c:1646  */
    break;

  case 11:
#line 125 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.str)=(yyvsp[-1].str); }
#line 1537 "y.tab.c" /* yacc.c:1646  */
    break;

  case 12:
#line 127 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.str)=(yyvsp[-1].str); }
#line 1543 "y.tab.c" /* yacc.c:1646  */
    break;

  case 13:
#line 129 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.ctype_ptr) = 0;}
#line 1549 "y.tab.c" /* yacc.c:1646  */
    break;

  case 14:
#line 130 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.ctype_ptr) = 0;}
#line 1555 "y.tab.c" /* yacc.c:1646  */
    break;

  case 15:
#line 131 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.ctype_ptr) = 0;}
#line 1561 "y.tab.c" /* yacc.c:1646  */
    break;

  case 16:
#line 132 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.ctype_ptr) = 0;}
#line 1567 "y.tab.c" /* yacc.c:1646  */
    break;

  case 17:
#line 133 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.ctype_ptr) = 0;}
#line 1573 "y.tab.c" /* yacc.c:1646  */
    break;

  case 27:
#line 144 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-3].str))+strlen((yyvsp[-2].str))+6); sprintf((yyval.str),"%s < %s >",(yyvsp[-3].str),(yyvsp[-1].str)); }
#line 1579 "y.tab.c" /* yacc.c:1646  */
    break;

  case 28:
#line 145 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+3); sprintf((yyval.str),"%s *",(yyvsp[-1].str)); }
#line 1585 "y.tab.c" /* yacc.c:1646  */
    break;

  case 29:
#line 146 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+3); sprintf((yyval.str),"%s &",(yyvsp[-1].str)); }
#line 1591 "y.tab.c" /* yacc.c:1646  */
    break;

  case 30:
#line 149 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-2].str))+strlen((yyvsp[0].str))+3); sprintf((yyval.str),"%s, %s",(yyvsp[-2].str),(yyvsp[0].str)); }
#line 1597 "y.tab.c" /* yacc.c:1646  */
    break;

  case 33:
#line 154 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+strlen((yyvsp[0].str))+2); sprintf((yyval.str),"%s %s",(yyvsp[-1].str),(yyvsp[0].str)); }
#line 1603 "y.tab.c" /* yacc.c:1646  */
    break;

  case 34:
#line 155 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+strlen((yyvsp[0].str))+2); sprintf((yyval.str),"%s %s",(yyvsp[-1].str),(yyvsp[0].str)); }
#line 1609 "y.tab.c" /* yacc.c:1646  */
    break;

  case 35:
#line 156 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+strlen((yyvsp[0].str))+2); sprintf((yyval.str),"%s %s",(yyvsp[-1].str),(yyvsp[0].str)); }
#line 1615 "y.tab.c" /* yacc.c:1646  */
    break;

  case 36:
#line 157 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+strlen((yyvsp[0].str))+2); sprintf((yyval.str),"%s %s",(yyvsp[-1].str),(yyvsp[0].str)); }
#line 1621 "y.tab.c" /* yacc.c:1646  */
    break;

  case 37:
#line 158 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+strlen((yyvsp[0].str))+2); sprintf((yyval.str),"%s %s",(yyvsp[-1].str),(yyvsp[0].str)); }
#line 1627 "y.tab.c" /* yacc.c:1646  */
    break;

  case 38:
#line 159 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+strlen((yyvsp[0].str))+2); sprintf((yyval.str),"%s %s",(yyvsp[-1].str),(yyvsp[0].str)); }
#line 1633 "y.tab.c" /* yacc.c:1646  */
    break;

  case 39:
#line 160 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+strlen((yyvsp[0].str))+2); sprintf((yyval.str),"%s %s",(yyvsp[-1].str),(yyvsp[0].str)); }
#line 1639 "y.tab.c" /* yacc.c:1646  */
    break;

  case 40:
#line 161 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+strlen((yyvsp[0].str))+2); sprintf((yyval.str),"%s %s",(yyvsp[-1].str),(yyvsp[0].str)); }
#line 1645 "y.tab.c" /* yacc.c:1646  */
    break;

  case 41:
#line 162 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+strlen((yyvsp[0].str))+2); sprintf((yyval.str),"%s %s",(yyvsp[-1].str),(yyvsp[0].str)); }
#line 1651 "y.tab.c" /* yacc.c:1646  */
    break;

  case 42:
#line 163 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+strlen((yyvsp[0].str))+2); sprintf((yyval.str),"%s %s",(yyvsp[-1].str),(yyvsp[0].str)); }
#line 1657 "y.tab.c" /* yacc.c:1646  */
    break;

  case 43:
#line 164 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+strlen((yyvsp[0].str))+2); sprintf((yyval.str),"%s %s",(yyvsp[-1].str),(yyvsp[0].str)); }
#line 1663 "y.tab.c" /* yacc.c:1646  */
    break;

  case 44:
#line 165 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-1].str))+strlen((yyvsp[0].str))+2); sprintf((yyval.str),"%s %s",(yyvsp[-1].str),(yyvsp[0].str)); }
#line 1669 "y.tab.c" /* yacc.c:1646  */
    break;

  case 54:
#line 177 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-2].str))+strlen((yyvsp[0].str))+3); sprintf((yyval.str),"%s::%s",(yyvsp[-2].str),(yyvsp[0].str)); }
#line 1675 "y.tab.c" /* yacc.c:1646  */
    break;

  case 56:
#line 181 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {(yyval.str) = (char*)malloc(strlen((yyvsp[-2].str))+strlen((yyvsp[0].str))+3); sprintf((yyval.str),"%s::%s",(yyvsp[-2].str),(yyvsp[0].str)); }
#line 1681 "y.tab.c" /* yacc.c:1646  */
    break;

  case 61:
#line 187 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.var_ptr) = addConfigVar(cur_ctype, 1, 1, 0, 0, 0, (yyvsp[-1].str), (yyvsp[0].str), 0, 0, 0, 0, 0); }
#line 1687 "y.tab.c" /* yacc.c:1646  */
    break;

  case 62:
#line 188 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.var_ptr) = addConfigVar(cur_ctype, 1, 1, 0, 0, 0, (yyvsp[-1].str), (yyvsp[0].str), 0, 0, 0, 0, 0); }
#line 1693 "y.tab.c" /* yacc.c:1646  */
    break;

  case 63:
#line 189 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.var_ptr) = addConfigVar(cur_ctype, 0, 1, 0, 0, 0, (yyvsp[-1].str), (yyvsp[0].str), 0, 0, 0, 0, 0); }
#line 1699 "y.tab.c" /* yacc.c:1646  */
    break;

  case 64:
#line 190 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.var_ptr) = addConfigVar(cur_ctype, 1, 0, 0, 0, 0, (yyvsp[-1].str), (yyvsp[0].str), 0, 0, 0, 0, 0); }
#line 1705 "y.tab.c" /* yacc.c:1646  */
    break;

  case 65:
#line 191 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.var_ptr) = addConfigVar(cur_ctype, 0, 0, 0, 0, 0, (yyvsp[-1].str), (yyvsp[0].str), 0, 0, 0, 0, 0); }
#line 1711 "y.tab.c" /* yacc.c:1646  */
    break;

  case 66:
#line 193 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {cur_cvar=(yyvsp[-1].var_ptr);}
#line 1717 "y.tab.c" /* yacc.c:1646  */
    break;

  case 67:
#line 194 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { 
			(yyval.var_ptr) = cur_cvar;
			if(0==(yyval.var_ptr)->config_type) {
				figure_out_config_type((yyval.var_ptr));
			}
		}
#line 1728 "y.tab.c" /* yacc.c:1646  */
    break;

  case 68:
#line 201 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {
			(yyvsp[-2].var_ptr)->raw_string=strdup((yyvsp[0].str));
			(yyval.var_ptr) = (yyvsp[-2].var_ptr);
			if(0==(yyval.var_ptr)->config_type) {
				figure_out_config_type((yyval.var_ptr));
			}
		}
#line 1740 "y.tab.c" /* yacc.c:1646  */
    break;

  case 69:
#line 209 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {
			(yyval.var_ptr) = (yyvsp[0].var_ptr);
			if(0==(yyval.var_ptr)->config_type) {
				figure_out_config_type((yyval.var_ptr));
			}
		}
#line 1751 "y.tab.c" /* yacc.c:1646  */
    break;

  case 70:
#line 216 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cvar->default_value=strdup((yyvsp[-1].str)); (yyval.var_ptr) = cur_cvar; }
#line 1757 "y.tab.c" /* yacc.c:1646  */
    break;

  case 71:
#line 217 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cvar->doc_string=strdup((yyvsp[-1].str)); (yyval.var_ptr) = cur_cvar; }
#line 1763 "y.tab.c" /* yacc.c:1646  */
    break;

  case 72:
#line 218 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cvar->config_type=strdup((yyvsp[-1].str)); (yyval.var_ptr) = cur_cvar; }
#line 1769 "y.tab.c" /* yacc.c:1646  */
    break;

  case 73:
#line 219 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cvar->unserialize_fct=strdup((yyvsp[-1].str)); (yyval.var_ptr) = cur_cvar; }
#line 1775 "y.tab.c" /* yacc.c:1646  */
    break;

  case 74:
#line 220 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cvar->serialize_fct=strdup((yyvsp[-1].str)); (yyval.var_ptr) = cur_cvar; }
#line 1781 "y.tab.c" /* yacc.c:1646  */
    break;

  case 75:
#line 221 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {cur_cvar->is_visualized=(strcmp((yyvsp[-1].str),"true")==0)?1:(strcmp((yyvsp[-1].str),"TRUE")==0?1:0); (yyval.var_ptr) = cur_cvar; }
#line 1787 "y.tab.c" /* yacc.c:1646  */
    break;

  case 76:
#line 222 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {cur_cvar->is_stat=(strcmp((yyvsp[-1].str),"true")==0)?1:(strcmp((yyvsp[-1].str),"TRUE")==0?1:0); (yyval.var_ptr) = cur_cvar; }
#line 1793 "y.tab.c" /* yacc.c:1646  */
    break;

  case 77:
#line 223 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cvar->default_value=strdup((yyvsp[-1].str)); (yyval.var_ptr) = cur_cvar; }
#line 1799 "y.tab.c" /* yacc.c:1646  */
    break;

  case 78:
#line 224 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cvar->doc_string=strdup((yyvsp[-1].str)); (yyval.var_ptr) = cur_cvar; }
#line 1805 "y.tab.c" /* yacc.c:1646  */
    break;

  case 79:
#line 225 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cvar->config_type=strdup((yyvsp[-1].str)); (yyval.var_ptr) = cur_cvar; }
#line 1811 "y.tab.c" /* yacc.c:1646  */
    break;

  case 80:
#line 226 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cvar->unserialize_fct=strdup((yyvsp[-1].str)); (yyval.var_ptr) = cur_cvar; }
#line 1817 "y.tab.c" /* yacc.c:1646  */
    break;

  case 81:
#line 227 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cvar->serialize_fct=strdup((yyvsp[-1].str)); (yyval.var_ptr) = cur_cvar; }
#line 1823 "y.tab.c" /* yacc.c:1646  */
    break;

  case 82:
#line 228 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cvar->is_visualized=(strcmp((yyvsp[-1].str),"true")==0)?1:(strcmp((yyvsp[-1].str),"TRUE")==0?1:0); (yyval.var_ptr) = cur_cvar; }
#line 1829 "y.tab.c" /* yacc.c:1646  */
    break;

  case 83:
#line 229 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cvar->is_stat=(strcmp((yyvsp[-1].str),"true")==0)?1:(strcmp((yyvsp[-1].str),"TRUE")==0?1:0); (yyval.var_ptr) = cur_cvar; }
#line 1835 "y.tab.c" /* yacc.c:1646  */
    break;

  case 84:
#line 233 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cchild =  addConfigChild(cur_ctype, (yyvsp[-3].str), (yyvsp[-1].str), 0, 0, 0, 0 ); }
#line 1841 "y.tab.c" /* yacc.c:1646  */
    break;

  case 85:
#line 234 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { (yyval.child_ptr) = cur_cchild; }
#line 1847 "y.tab.c" /* yacc.c:1646  */
    break;

  case 86:
#line 236 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cchild->min=strdup((yyvsp[-1].str)); (yyval.child_ptr) = 0; }
#line 1853 "y.tab.c" /* yacc.c:1646  */
    break;

  case 87:
#line 237 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cchild->max=strdup((yyvsp[-1].str)); (yyval.child_ptr) = 0; }
#line 1859 "y.tab.c" /* yacc.c:1646  */
    break;

  case 88:
#line 238 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {
				cur_cchild->is_aliased=false;
				if(0!=strcasecmp("false",(yyvsp[-1].str)) && 0!=strcmp("0",(yyvsp[-1].str))) {
					cur_cchild->is_aliased=true;
				}
				(yyval.child_ptr) = 0;
			}
#line 1871 "y.tab.c" /* yacc.c:1646  */
    break;

  case 89:
#line 245 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cchild->doc_string=strdup((yyvsp[-1].str)); (yyval.child_ptr) = 0; }
#line 1877 "y.tab.c" /* yacc.c:1646  */
    break;

  case 90:
#line 246 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cchild->min=strdup((yyvsp[-1].str)); (yyval.child_ptr) = 0; }
#line 1883 "y.tab.c" /* yacc.c:1646  */
    break;

  case 91:
#line 247 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cchild->max=strdup((yyvsp[-1].str)); (yyval.child_ptr) = 0; }
#line 1889 "y.tab.c" /* yacc.c:1646  */
    break;

  case 92:
#line 248 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    {
				cur_cchild->is_aliased=false;
				if(0!=strcasecmp("false",(yyvsp[-1].str)) && 0!=strcmp("0",(yyvsp[-1].str))) {
					cur_cchild->is_aliased=true;
				}
				(yyval.child_ptr) = 0;
			}
#line 1901 "y.tab.c" /* yacc.c:1646  */
    break;

  case 93:
#line 255 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1646  */
    { cur_cchild->doc_string=strdup((yyvsp[-1].str)); (yyval.child_ptr) = 0; }
#line 1907 "y.tab.c" /* yacc.c:1646  */
    break;


#line 1911 "y.tab.c" /* yacc.c:1646  */
      default: break;
    }
  /* User semantic actions sometimes alter yychar, and that requires
     that yytoken be updated with the new translation.  We take the
     approach of translating immediately before every use of yytoken.
     One alternative is translating here after every semantic action,
     but that translation would be missed if the semantic action invokes
     YYABORT, YYACCEPT, or YYERROR immediately after altering yychar or
     if it invokes YYBACKUP.  In the case of YYABORT or YYACCEPT, an
     incorrect destructor might then be invoked immediately.  In the
     case of YYERROR or YYBACKUP, subsequent parser actions might lead
     to an incorrect destructor call or verbose syntax error message
     before the lookahead is translated.  */
  YY_SYMBOL_PRINT ("-> $$ =", yyr1[yyn], &yyval, &yyloc);

  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);

  *++yyvsp = yyval;

  /* Now 'shift' the result of the reduction.  Determine what state
     that goes to, based on the state we popped back to and the rule
     number reduced by.  */

  yyn = yyr1[yyn];

  yystate = yypgoto[yyn - YYNTOKENS] + *yyssp;
  if (0 <= yystate && yystate <= YYLAST && yycheck[yystate] == *yyssp)
    yystate = yytable[yystate];
  else
    yystate = yydefgoto[yyn - YYNTOKENS];

  goto yynewstate;


/*--------------------------------------.
| yyerrlab -- here on detecting error.  |
`--------------------------------------*/
yyerrlab:
  /* Make sure we have latest lookahead translation.  See comments at
     user semantic actions for why this is necessary.  */
  yytoken = yychar == YYEMPTY ? YYEMPTY : YYTRANSLATE (yychar);

  /* If not already recovering from an error, report this error.  */
  if (!yyerrstatus)
    {
      ++yynerrs;
#if ! YYERROR_VERBOSE
      yyerror (YY_("syntax error"));
#else
# define YYSYNTAX_ERROR yysyntax_error (&yymsg_alloc, &yymsg, \
                                        yyssp, yytoken)
      {
        char const *yymsgp = YY_("syntax error");
        int yysyntax_error_status;
        yysyntax_error_status = YYSYNTAX_ERROR;
        if (yysyntax_error_status == 0)
          yymsgp = yymsg;
        else if (yysyntax_error_status == 1)
          {
            if (yymsg != yymsgbuf)
              YYSTACK_FREE (yymsg);
            yymsg = (char *) YYSTACK_ALLOC (yymsg_alloc);
            if (!yymsg)
              {
                yymsg = yymsgbuf;
                yymsg_alloc = sizeof yymsgbuf;
                yysyntax_error_status = 2;
              }
            else
              {
                yysyntax_error_status = YYSYNTAX_ERROR;
                yymsgp = yymsg;
              }
          }
        yyerror (yymsgp);
        if (yysyntax_error_status == 2)
          goto yyexhaustedlab;
      }
# undef YYSYNTAX_ERROR
#endif
    }



  if (yyerrstatus == 3)
    {
      /* If just tried and failed to reuse lookahead token after an
         error, discard it.  */

      if (yychar <= YYEOF)
        {
          /* Return failure if at end of input.  */
          if (yychar == YYEOF)
            YYABORT;
        }
      else
        {
          yydestruct ("Error: discarding",
                      yytoken, &yylval);
          yychar = YYEMPTY;
        }
    }

  /* Else will try to reuse lookahead token after shifting the error
     token.  */
  goto yyerrlab1;


/*---------------------------------------------------.
| yyerrorlab -- error raised explicitly by YYERROR.  |
`---------------------------------------------------*/
yyerrorlab:

  /* Pacify compilers like GCC when the user code never invokes
     YYERROR and the label yyerrorlab therefore never appears in user
     code.  */
  if (/*CONSTCOND*/ 0)
     goto yyerrorlab;

  /* Do not reclaim the symbols of the rule whose action triggered
     this YYERROR.  */
  YYPOPSTACK (yylen);
  yylen = 0;
  YY_STACK_PRINT (yyss, yyssp);
  yystate = *yyssp;
  goto yyerrlab1;


/*-------------------------------------------------------------.
| yyerrlab1 -- common code for both syntax error and YYERROR.  |
`-------------------------------------------------------------*/
yyerrlab1:
  yyerrstatus = 3;      /* Each real token shifted decrements this.  */

  for (;;)
    {
      yyn = yypact[yystate];
      if (!yypact_value_is_default (yyn))
        {
          yyn += YYTERROR;
          if (0 <= yyn && yyn <= YYLAST && yycheck[yyn] == YYTERROR)
            {
              yyn = yytable[yyn];
              if (0 < yyn)
                break;
            }
        }

      /* Pop the current state because it cannot handle the error token.  */
      if (yyssp == yyss)
        YYABORT;


      yydestruct ("Error: popping",
                  yystos[yystate], yyvsp);
      YYPOPSTACK (1);
      yystate = *yyssp;
      YY_STACK_PRINT (yyss, yyssp);
    }

  YY_IGNORE_MAYBE_UNINITIALIZED_BEGIN
  *++yyvsp = yylval;
  YY_IGNORE_MAYBE_UNINITIALIZED_END


  /* Shift the error token.  */
  YY_SYMBOL_PRINT ("Shifting", yystos[yyn], yyvsp, yylsp);

  yystate = yyn;
  goto yynewstate;


/*-------------------------------------.
| yyacceptlab -- YYACCEPT comes here.  |
`-------------------------------------*/
yyacceptlab:
  yyresult = 0;
  goto yyreturn;

/*-----------------------------------.
| yyabortlab -- YYABORT comes here.  |
`-----------------------------------*/
yyabortlab:
  yyresult = 1;
  goto yyreturn;

#if !defined yyoverflow || YYERROR_VERBOSE
/*-------------------------------------------------.
| yyexhaustedlab -- memory exhaustion comes here.  |
`-------------------------------------------------*/
yyexhaustedlab:
  yyerror (YY_("memory exhausted"));
  yyresult = 2;
  /* Fall through.  */
#endif

yyreturn:
  if (yychar != YYEMPTY)
    {
      /* Make sure we have latest lookahead translation.  See comments at
         user semantic actions for why this is necessary.  */
      yytoken = YYTRANSLATE (yychar);
      yydestruct ("Cleanup: discarding lookahead",
                  yytoken, &yylval);
    }
  /* Do not reclaim the symbols of the rule whose action triggered
     this YYABORT or YYACCEPT.  */
  YYPOPSTACK (yylen);
  YY_STACK_PRINT (yyss, yyssp);
  while (yyssp != yyss)
    {
      yydestruct ("Cleanup: popping",
                  yystos[*yyssp], yyvsp);
      YYPOPSTACK (1);
    }
#ifndef yyoverflow
  if (yyss != yyssa)
    YYSTACK_FREE (yyss);
#endif
#if YYERROR_VERBOSE
  if (yymsg != yymsgbuf)
    YYSTACK_FREE (yymsg);
#endif
  return yyresult;
}
#line 257 "/home/liux/primogeni/netsim/tools/ssfnet-xlate/generator.y" /* yacc.c:1906  */

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
