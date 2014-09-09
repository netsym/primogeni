/*----------------------------------------------------------------------------*/
/* Author(s): Kalyan Perumalla <http://www.cc.gatech.edu/~kalyan> 04Oct2003   */
/* $Revision: 1.1.1.1 $ $Name:  $ $Date: 2004/07/21 21:01:31 $ */
/*----------------------------------------------------------------------------*/
#include <string.h>

#include "mycompat.h"

/*----------------------------------------------------------------------------*/
#if PLATFORM_WINCE
#define MAXENVS 64
static char *enames[MAXENVS];
static char *evals[MAXENVS];
static int nenvs = 0;
static int find( const char *s )
{
    int i = 0;
    for( i = 0; i < nenvs; i++ ) if( !strcmp( enames[i], s ) ) return i;
    return -1;
}
char *getenv( const char *s )
{
    int i = 0;
    char *match = 0;
    if( (i=find(s)) >= 0 ) match=evals[i];
    return match;
}
int putenv( char *s )
{
    int status = 0, i = 0;
    char *name = s, *val = strchr(s,'=');
    if(!val) val = ""; else {*val=0; val++;}
    i = find( name );
    if( i >= 0 )
    {
        evals[i] = val;
    }
    else
    {
        if( nenvs < MAXENVS )
        {
            enames[nenvs] = name;
            evals[nenvs++] = val;
        }
        else
        {
            status = -1;
        }
    }
    return status;
}
void assert_callback( const TCHAR *fname, int linenum, const TCHAR *condition )
{
    HWND hWnd = NULL;
    TCHAR linestr[100]; _itow(linenum,linestr,10);
    MessageBox( hWnd, condition, TEXT("Failed assertion"),
           MB_OK | MB_ICONSTOP | MB_APPLMODAL | MB_SETFOREGROUND | MB_TOPMOST );
    MessageBox( hWnd, fname, TEXT("Assertion file"),
           MB_OK | MB_ICONSTOP | MB_APPLMODAL | MB_SETFOREGROUND | MB_TOPMOST );
    MessageBox( hWnd, linestr, TEXT("Assertion line"),
           MB_OK | MB_ICONSTOP | MB_APPLMODAL | MB_SETFOREGROUND | MB_TOPMOST );
    exit(1);
}

#endif

/*----------------------------------------------------------------------------*/
