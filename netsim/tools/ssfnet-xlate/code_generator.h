#ifndef __SCHEMA_H__
#define __SCHEMA_H__

struct config_type;
struct config_var;
struct config_child;

#define IMPL_ID 0
#define ALIAS_ID 1
#define REPLICA_ID 2
#define ALIAS_REPLICA_ID 3

#define _JPRIME_PROPERTY 50
#define _JPRIME_SYMBOL_TABLE 51
#define _JPRIME_PARTITION 52
#define _JPRIME_COMMUNITY 53
#define _JPRIME_SYMBOL_TABLE_ENTRY 54
#define _JPRIME_ROUTE_TABLE 55
#define _JPRIME_ROUTE_ENTRY 56
#define _JPRIME_GHOST_NODE 57
#define _JPRIME_GENERIC_NODE 58


struct config_type {
	char* name;
	char* name_tpl;
	char* extends;
	char* extends_tpl;
	char* type_id;
	char* alias;
	char* filename;
	int user_type_id;
	int auto_type_ids[4];

	struct config_type* extends_ptr;
	struct config_type* parent;
	struct config_type* next;
	struct config_type* prev;

	struct config_type* first_sub_type;
	struct config_type* last_sub_type;

	struct config_var* first_var;
	struct config_var* last_var;

	struct config_child* first_child;
	struct config_child* last_child;

};

struct config_var {
	char* config_type;
	char* c_type;
	char* name;
	char* default_value;
	char* serialize_fct;
	char* unserialize_fct;
	char* doc_string;
	char* raw_string;
	short is_configurable;
	short is_shared;
	short is_visualized;
	short is_stat;
	struct config_type *parent;
	struct config_var *next;
	struct config_var *prev;
	long var_id;
};

struct config_child {
	char* c_type;
	char* name;
	char* doc_string;
	char* min;
	char* max;
	bool is_aliased;
	struct config_type* the_type;
	struct config_type *parent;
	struct config_child *next;
	struct config_child *prev;
};

struct temp_ll {
	struct config_type* data;
	struct temp_ll* next;
};

typedef struct config_type config_type_t;
typedef struct config_var config_var_t;
typedef struct config_child config_child_t;
typedef struct temp_ll temp_ll_t;

config_type_t* getHead();

config_type_t* addConfigType(char* cls_name, char* cls_tpl, char* extends, char* extends_tpl, char* type_id, char* alias, char* filename);

config_var_t* addConfigVar(config_type_t* parent, short is_shared, short is_configurable, short is_visualized, short is_stat, char* config_type, char* c_type, char* name, char* default_value, char* serialize_fct, char* unserialize_fct, char* doc_string, char* raw_string);

config_child_t* addConfigChild(config_type_t* parent, char* c_type, char* name, char* min, char* max, char* is_aliased, char* doc_string);

void figure_out_config_type(config_var_t* t);

int __main__(int argc, char** argv);

void generateCPPCode(char* target_h_file, char* target_cc_file);
void generateJavaCode(char* jprime_dir);


#endif
