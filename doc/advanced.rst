.. meta::
   :description: Advanced Usage of PrimoGENI
   :keywords: PrimoGENI, simulation, emulation, network simulation, network emulation, PRIME, PRIMEX, SSFNet

.. _advanced-label:

************
Advanced Usage of PrimoGENI
************

PrimoGENI is composed of four main components:

* **PRIMEX**: The network simulator.

* **jPRIME**: The network model configuration library. 

* **Meta-Controller**: The experiment controller

* **OS Image(s)**: A OpenVZ based OS image which allows the simulator to setup virtual machines for use in emulation based network experiments.

* **Slingshot**: The eclipse-based GUI which presents frontend to the previous four components.

PRIMEX, jPRIME and the Meta-Controller are each stand-alone applications which can setup and run in a variety of ways. Slingshot supports a small number of possible scenarios in which PrimoGENI could be used. Below we will cover some basics of PRIMEX, jPRIME and the Meta-Controller so you wil be able to use PrimoGENI in your specific computing environment.


.. _command-line-compilation-label:

===========================================
Model Compilation and Partitioning
===========================================

It is assumed that you  have followed the instructions in the :ref:`install-label` section so you have a working version of the PrimoGENI. Below are details on how to construct, compile, and partition network models from the command line.

------------------------------
TLVMaker
------------------------------

While compiling PrimoGENI you compiled jPRIME and the Meta-Controller in **primex/netscript**. The resulting JAR and its dependancies are found in **primex/netscript/dist**. By default **jprime.jar** uses :jprime:`TLVMaker  <jprime::TLVMaker>` as the main class so when you call jprime.jar you are really calling the :jprime:`TLVMaker  <jprime::TLVMaker>`. 

The TLVMaker is able to create, load, and delete network models from a model database.

* **create** : This command will take a network model file, create it in the database, then compile it into a TLV(s) for execution with PRIMEX. The model files can be a **java**, **class**, **python**, or **xml** file.  When you creating new models the TLVMaker will replace old experiments with the same name. 

   .. note:: When creating new experiments from the command line they will not show up in Slingshot. Slingshot maintains a local cache of experimental meta-data outside of the database which is not updated by the TLVMaker. In later releases will allow slingshot to synchronize with models created from the command line.

* **load** : This command will load a pre-existing model from the the database and create TLV(s) for execution with PRIMEX.

* **delete** : This command will delete the model from the database.

To get a list of all the options supported by the TLVMaker first go **primex/netscript** and then type::

   % java -jar dist/jprime.jar

To create, compile, and Partition  :jprime:`MySecondJavaModel.java` to run on a single machine you would type::

  % java -jar dist/jprime.jar create foobar test/java_models/MySecondJavaModel.java

which would create the experiment **foobar** in the default model database and create  **foobar_part_1.tlv**, **foobar_part_1.xml** and **foobar.xml**. foobar_part_1.xml is a human readable version of the TLV mostly used for debugging. foobar.xml is an XML version of the experiment that can be used to create another model. For large models it can be quite expensive to create the xml files. To compile the model without producing the XMLs we could do::

  % java -jar -DCREATE_XML=false dist/jprime.jar load foobar

We used "load" this time becuase the model was already in our database. However, we could also used the "-DCREATE_XML=false" option when creating the model in the database as well.

------------------------------
TLVMaker Properties
------------------------------

All options for the TLVMaker are passed in via the Java properties. Java properties can be passed using "-D<option_name>=<option_value>" from the command line. Below is a list of the currently supported options.

* Database Options:

  * DB_TYPE: which type of db to use (options are DERBY or MYSQL), defaults to 'DERBY'
  * DB_NAME:  the name of the database, defaults to 'primex'.
  * DB_PATH: where to store the db, defaults to '/tmp'. Only makes sense when DB_TYPE is DERBY.
  * DB_SERVER: (MySQL) the mysql server ip, defaults to 'localhost'. Only makes sense when DB_TYPE is MYSQL.
  * DB_PORT: (MySQL) the server port, if not specified the default MySQL port is used. Only makes sense when DB_TYPE is MYSQL.
  * DB_USER: the name of the database, defaults to 'primex'.
  * DB_PASS: the name of the database, defaults to 'passwd'.
  * DB_DEBUG:  whether to debug the db, defaults to 'false'.

* Object/Database Caching Options:

  * DB_LOG_ACCESS: log access patterns, defaults to 'false'.
  * DB_CACHE_TYPE: the type of cache to use, defaults to 'FIFO';  other options are 'LRU'. 'COMPLETE', or 'FREQ'.
  * MEM_THRESHOLD: the cache's high watermark, defaults to '0.70'.
  * DB_BATCH_SIZE: the # of objects per transaction, defaults to '10000'.
  * DB_FECTCH_SIZE: the # of objects to fetch at once, defaults to '1'.
  * LOG_DIR: the directory to store logs at, defaults to '/tmp'.
  * LOG_PREFIX: prefix for logs.

* Compilation & Partitioning Options:

  * CREATE_XML: create a xml's during compilation, defaults to 'true'.
  * PART_STR: how to partition the model, defaults to '1::1:1'. The
    format of the string is
    **<num_partitions>::<part_id::num_procs_to_use>,...,<part_id::num_procs_to_use>** or **<num_partitions>::*::<procs_on_each_compute_node>** .
  * OUT_DIR: where to store the TLV and XML files, defaults to '.'.

* Runtime Environment Options (used for experiments involving emulation):

  * RUNTIME_ENV_FILE: a file containing the runtime environment specification, defaults to 'null'. These files are created by Slingshot and can be found your Slingshot workspace.
  * RUNTIME_ENV: a string which defines a master and slave compute nodes, defaults to 'null'. RUNTIME_ENV is composed of a list of compute nodes. The following regex defines a compute node string: **(\[control_ip,data_ip(,portal_name,portal_ip)*\])+**
  * PORTAL_LINKS: a string which which portals are connected to which portals, defaults to 'null'. The following regex defines the compute node string: **(control_ip:portal_name,interface_name)+**

------------------------------
Examples
------------------------------

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Multiple Partitions
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

* Compile SomeModel.java to run with two partitions::

   % java -DPART_STR="2::1:1,2:1" -jar dist/jprime.jar  create some_model_name SomeModel.java

* Alternatively, you can specify the partitioning string like ``2::*:1`` which means all compute node will have 1 processor.::

   % java -DPART_STR="2::*:1" -jar dist/jprime.jar  create some_model_name SomeModel.java

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Multiple Partition with Traffic Portals
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

* Compile SomeModel.java to run with two partitions where each partition will have a traffic portal::

  % java -DPART_STR="2::1:1,2:1" -DRUNTIME_ENV="[c_master,d_master],[c_slave1,d_slave1,eth2,10.10.2.2],[c_slave2,d_slave2,eth3,10.10.2.3]" -DPORTAL_LINKS="c_slave1:eth2,topnet.host1.if0,c_slave2:eth3,topnet.sub1.h1.if2" -jar dist/jprime.jar -jar dist/jprime.jar create some_model_name SomeModel.java

* Compile SomeModel.java to run with  partitions but 1 partition has two traffic portals::

  % java -DPART_STR="2::1:1,2:1"-DRUNTIME_ENV="[c_master,d_master],[c_slave1,d_slave1,eth2,10.10.2.2,eth3,10.10.2.3],[c_slave2,d_slave2]" -D PORTAL_LINKS="c_slave1:eth2,topnet.host1.if0,c_slave1:eth3,topnet.sub1.h1.if2 -jar dist/jprime.jar create some_model_name SomeModel.java


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Switching Model Databases
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

PrimoGENI currently supports `MySQL <https://www.mysql.com>`_ and
`Apache Derby <http://db.apache.org/derby/>`_ databases. The default
database is Derby. To use MySQL one must

* Setup a MySQL server.
* Setup a database for PrimoGENI to use (we recommend primex).
* Setup a user which is able to create tables within the database.

Assume the MySQL database is correctly setup on the machine where the
model will be compiled, we could compile MySecondJavaModel.java using
the following command::

  % java -DDB_TYPE=MYSQL -DDB_NAME=primex -DDB_USER=primex -DDB_PASS=passwd -DDB_SERVER=localhost -jar dist/jprime.jar create foobar test/java_models/MySecondJavaModel.java


.. _command-line-running-label:

===========================================
Running Simulations
===========================================

PRIMEX uses `pthreads <http://en.wikipedia.org/wiki/POSIX_Threads>`_
to run in parallel on shared memory multi-processors and `MPI <http://en.wikipedia.org/wiki/Message_Passing_Interface>`_  
to run in parallel on distributed memory machines and compute
clusters.  

------------------------------
PRIMEX Command line Options
------------------------------

* **Usage:** <primo_install_dir>/primex [ssf-options] [ssfnet-options] <sim-time> <model source>
* **SSF command-line options:**

  * -submodel <fname>  : use ssf dml submodel
  * -s : same as -submodel
  * -nmachs <m> [n_0,]p_0:[n_1,]p_1:..:[n_{m-1},]p_{m-1} : machine config (default: 1 localhost:1)
  * -m : same as -nmachs
  * -nprocs <n> : set number of processors (default: 1)
  * -n : same as -nprocs
  * -rank <r> : set the machine rank in the array (default: 0 or from MPI context)
  * -r : same as -rank
  * -same : homogeneous machine cluster
  * -diverse : heterogeneous machine cluster (default)
  * -seed <n> : set initial random seed (default: 54321)
  * -silent : suppress ssf messages (default: show messages)
  * -showcfg : show system configuration (default)
  * -noshowcfg : do not show system configuration
  * -outfile <fname> : output message to file (default: stdout)
  * -o : same as -outfile
  * -debug <n>: debug mask (default 0)
  * -d : same as -debug
  * -flowdelta <ltime> : time increment per scheduling (default: infinite)
  * -flowmark <mbytes> : max kernel events per scheduling (default: infinite)
  * -progress <intv> : show progress of simulation (default: infinite)
  * -p : same as -progress
  * -synthresh <ltime> : local composite synchronization threshold (default: automatic)
  * -t : same as -synthresh
  * -spin : spinning simulation processes instead of yielding
  * -yield : yielding simulation processes (default)
  * -- : end of parsing ssf command-line arguments

* **SSFNet command-line options:**

  * -help : show command line options
  * -h : same as -help
  * -quiet : quiet mode (no ssfnet runtime messages)
  * -q : same as -quiet
  * -emuratio <ratio> : set emulation speed (i.e., the slow-down factor).
  * -e : same as -emuratio
  * -enable_state_file : save exported state to state_part_X.stats
  * -enable_state_stream : send exported state to a state server.
  * -stream_server <ip>:<port> : connect to the state sever at <ip>:<port> ( the default is 127.0.0.1:9992);
  * -state_dir <dir> : where to store state files (defaults to the location where the packed model files are stored).

* **<sim-time>:**  the simulation time in seconds.
* **<model source>:** specify ONE of the following

 * <server>:<port> : The name of the server that hosts the model DB, and port which its runs on
 * <packed-file> : the serialized model for this partition
 * <name> <dir> : were name is the <name> of the model and <dir> is the location of the packed files for each partition.

------------------------------
Cluster/MPI Setup
------------------------------

To run PrimoGENI with MPI you must configure it to use MPI. It is
recommended that you use `MPICH2 <http://www.mcs.anl.gov/research/projects/mpich2/>`_ 
to compile and run PrimoGENI. We have only tested using MPICH2.
Below are two example commands to configure PrimoGENI in a cluster.

* Configure PRIMEX for a cluster environment where compute nodes **are**
  capable of running emulation (i.e. have `OpenVZ <http://openvz.org>`_ kernel extensions).::

  % cd <primex_dir>/netsim
  % ./configure --with-ssf-sync=mpi --disable-ssfnet-debug

* Configure PRIMEX for a cluster enviornment where compute nodes are
  **not** capable of running emulation.::

  % cd <primex_dir>/netsim
  % ./configure --with-ssf-sync=mpi --disable-ssfnet-debug --disable-ssfnet-emulation  --disable-ssfnet-openvpn


Recent installations of MPICH2 use `Hydra
<http://wiki.mcs.anl.gov/mpich2/index.php/Using_the_Hydra_Process_Manager>`_
as the default process manager. In the following examples we assume
that you are using Hydra. 

The default configuration of Hydra/MPICH2 is use ssl to connect the
different MPI processes. For this to work you must be able to ssh
from machine to machine without typing a password. There are two ways
to do this:

* Create a public/private key without a password.  The following creates a public/private key pair id_dsa/id_dsa.pub and places the public key in your authorized keys file so you can authenticate using the key. ::

  %  cd ~/.ssh
  % ssh-keygen -t dsa -f id_dsa
  % cat id_dsa.pub >> authorized_keys2
  % chmod 640 authorized_keys2

  * You can choose to use a password (or not) depending on how your key will be used.  If you use a password you must setup an ssh-agent and seed your key before you can run jobs. See below for details on how to do that.

* Use the ssh-agent to "seed" your key. ::

  % killall ssh-agent 
  % ssh-agent > tmp
  % source tmp
  % rm tmp
  % ssh-add ~/.ssh/id_dsa

  * Now you can ssh to another machine without typing your password using the seeded key.

Hyrdra needs to know about which machines to run on. Your
administrator may or may not have installed a default machine file in your
cluster. You specify your own machine file using "-f". A machine file
consists lines describing the compute you wish to execute on. Each
line contains a machine name and the number of available processors on
the machine (seperated by a colon). The following machine file defines
four computes, two with 4 processors and 2 with 2 processors::

  compute_node_1:4
  compute_node_2:4
  compute_node_3:2
  compute_node_4:2

To test your configuration lets run "hostname" on each node in your
machine file::

  % mpirun -f your_machine_file.txt -ppn 1 hostname

You should then see the names of each node in the cluster.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
PrimoGENI/PRIMEX Installation
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

We assume that the cluster has a shared filesystem. Within this shared
folder you need to "install" PrimoGENI.

* Create a folder in the shared file system, for example  ``/shared/primogeni/``.
* Get PrimoGENI and compile it in ``/shared/primogeni/`` (see :ref:`install-label` for more details on acquiring and compiling PrimoGENI).
* Create a folder to store experimental data ::

  % mkdir /shared/primogeni/exps

* When you a finished you should have the following directory structure:

  * ``/shared/primogeni/netscript``
  * ``/shared/primogeni/netsim``
  * ``/shared/primogeni/exps``
  * ``/shared/primogeni/topology``
  * ``/shared/primogeni/netIDE``
  * ``/shared/primogeni/doc``
  * ``/shared/primogeni/test``

* Each compute node that will run simulator instances should be able to execute ``/shared/primogeni/netsim/primex`` .

------------------------------
Examples
------------------------------

Below are a few examples of how to run :jprime:`NCampus.java`
on your cluster.  :jprime:`NCampus.java` has a number of
configurable properties to control how much traffic:

* NUM_CAMPUS : The number of campuses that will be in the resulting model.
* INTRA_CAMPUS_TRAFFIC : Whether pings between hosts in the same campus should be sent.
* INTER_CAMPUS_TRAFFIC: Whether pings between hosts in the different campus should be sent.

The following command would compile :jprime:`NCampus.java` with 20 campuses, pings between hosts within the same campus and pings between campuses, to run on 6 compute nodes ::

  % cd ``/shared/primogeni/netscript``
  % java -DPART_STR="6::*:1" -DNUM_CAMPUS=20 -DINTRA_CAMPUS_TRAFFIC=true -DINTER_CAMPUS_TRAFFIC=true  -DCREATE_XML=false -jar dist/jprime.jar  create 20Campus test/java_models/NCampus.java

You will then see ``20Campus_part_1.tlv``,  ``20Campus_part_2.tlv``,..., ``20Campus_part_6.tlv``.  Delete these, we will create more below.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Without exporting state from the simulation
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

* Create the model ::

  % cd ``/shared/primogeni/netscript``
  % mkdir ``/shared/primogeni/exps/exp1``
  % java -DOUT_DIR=/shared/primogeni/exps/exp1 -DPART_STR="6::*:1" -DNUM_CAMPUS=20 -DINTRA_CAMPUS_TRAFFIC=true -DINTER_CAMPUS_TRAFFIC=true  -DCREATE_XML=false -jar dist/jprime.jar  create 20Campus test/java_models/NCampus.java

   * The result should be ``/shared/primogeni/exps/exp1/20Campus_part_{1,2,3,4,5,6}.tlv``

* Run the simulation for 100 seconds (assuming you have yours keys
  and machine file properly setup)::

  % mpirun -f your_machine_file.txt -ppn 1 -n 6 /shared/primogeni/netsim/primex 100 20Campus /shared/primogeni/exps/exp1

  * The command will start a simulator on the first 6 nodes in the machine file. 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Exporting state from the simulation to a file
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

* Create the model ::

  % cd ``/shared/primogeni/netscript``
  % mkdir ``/shared/primogeni/exps/exp2``
  % java -DOUT_DIR=/shared/primogeni/exps/exp2 -DPART_STR="6::*:1" -DNUM_CAMPUS=20 -DINTRA_CAMPUS_TRAFFIC=true -DINTER_CAMPUS_TRAFFIC=true  -DCREATE_XML=false -jar dist/jprime.jar  create 20Campus test/java_models/NCampus.java

   * The result should be ``/shared/primogeni/exps/exp2/20Campus_part_{1,2,3,4,5,6}.tlv``

* Run the simulation for 100 seconds::

  % mpirun -f your_machine_file.txt -ppn 1 -n 6 /shared/primogeni/netsim/primex -enable_state_file 100 20Campus /shared/primogeni/exps/exp2

  * When the simulation is finished you should see  ``/shared/primogeni/exps/exp2/state_part_{1,2,3,4,5,6}.stats`` .

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Exporting state from the simulation via TCP
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

* Create the model ::

  % cd ``/shared/primogeni/netscript``
  % mkdir ``/shared/primogeni/exps/exp2``
  % java -DOUT_DIR=/shared/primogeni/exps/exp2 -DPART_STR="6::*:1" -DNUM_CAMPUS=20 -DINTRA_CAMPUS_TRAFFIC=true -DINTER_CAMPUS_TRAFFIC=true  -DCREATE_XML=false -jar dist/jprime.jar  create 20Campus test/java_models/NCampus.java

   * The result should be ``/shared/primogeni/exps/exp2/20Campus_part_{1,2,3,4,5,6}.tlv``

* PrimoGENI's meta controller comes with a simple Java program that will print out stats received on state streams from simulators. In a separate window run::

  % java -cp /shared/primogeni/netscript/jprime.jar monitor.util.PrimeStateServer

* Run the simulation for 100 seconds (replace XXX with the name of the machine where you ran the PrimeStateServer)::

  % mpirun -f your_machine_file.txt -ppn 1 -n 6 /shared/primogeni/netsim/primex -enable_state_stream -stream_server XXX:9992 100 20Campus /shared/primogeni/exps/exp2

  * :controller:`ModelInterface  <monitor::util::PrimeStateServer>` will print out state updates as they are received. You could use :controller:`ModelInterface <monitor::util::PrimeStateServer>` as a basis to create your own state listener.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Exporting state from the simulation via Meta-Controllers
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

* Install Slingshot (see :ref:`install-label` )
* Launch Slingshot (see :ref:`quick-start-label` )
* Create the model within Slingshot  (see :ref:`quick-start-label` )
* Start the meta-controllers on the compute nodes

  * When using meta-controllers we currently require an extra node to act as a "master" node. If you want run the experiment on 4 nodes that you should start 5 meta-controllers.
  * The meta-controller has a number of important properties that can  be set at the command line:

    * GEN_SSH_KEYS : whether to automatically generate ssh keys  (intended for use with emulab/protogeni compute nodes only)
    * NETSIM_DIR : the location of the simulator installation
    * BASE_EXP_DIR : where to store experiment info
    * PRIMOGENI_FOLDER : where the compute node scripts are
    * LOCAL_MONITOR_OUTPUT : where to store debug and logs
    * MACHINE_FILE : where to store the automatically generated mpi machine file (in future releases this will not need to be set)
    * PVS_CONFIG : where to store the automatically generated pvs configuration (in future releases this will not need to be set)

  * Assuming PrimoGENI has been installed at described above, you could start your meta-controllers using::

    % mpirun -f <a_machine_file_with_N_machines_in_it> -ppn 1 java -DGEN_SSH_KEYS=false -DNETSIM_DIR=/shared/primogeni/netsim -DBASE_EXP_DIR=/shared/primogeni/exps -DPRIMOGENI_FOLDER=/shared/primogeni/netscript/src/monitor_scripts -DLOCAL_MONITOR_OUTPUT=/tmp -DMACHINE_FILE=/tmp/machinefile -DPVS_CONFIG=/tmp/pvs.config -cp /shared/primogeni/netscript/dist/jprime.jar monitor.core.Monitor &

  * Where <a_machine_file_with_N_machines_in_it> is a machine file which contains the ip (or names) of the compute nodes you want to  run the simulation on.

  .. note:: The meta-controller will start the simulation using mpirun. If you do not use a password-less key, you need to ensure that the ssh-agent is properly seeded and the appropriate environment variable(s) are transferred via mpirun so that the meta-controller can issue subsequent mpirun calls.

* Create a "*Remote Cluster*" environment  (see  :ref:`slingshot-user-manual-label` )

  * Choose one of the machines in <a_machine_file_with_N_machines_in_it> to act as the master. The remaining machines will be slaves.

* Deploy the experiment (see :ref:`slingshot-user-manual-label` )
* The simulation should be running on the compute nodes and you should see traffic in Slingshot.
