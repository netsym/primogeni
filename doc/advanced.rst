.. meta::
   :description: Advanced Usage of PrimoGENI
   :keywords: PrimoGENI, simulation, emulation, network simulation, network emulation, PRIME, PRIMEX, SSFNet

.. _advanced-label:

***************************
Advanced Usage of PrimoGENI
***************************

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

   % java -DPART_STR="2::1:1,2:1" -jar dist/jprime.jar create some_model_name SomeModel.java

* Alternatively, you can specify the partitioning string like ``2::*:1`` which means all compute node will have 1 processor.::

   % java -DPART_STR="2::*:1" -jar dist/jprime.jar create some_model_name SomeModel.java

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
  % ./configure --with-ssf-sync=mpi --enable-ssfnet-openvpn --disable-lzo

* Configure PRIMEX for a cluster enviornment where compute nodes are
  **not** capable of running emulation.::

  % cd <primex_dir>/netsim
  % ./configure --with-ssf-sync=mpi --disable-ssfnet-emulation


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

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
Exporting state from the simulation via Meta-Controllers
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

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

=====================================
Configuring Transport Protocols
=====================================

If your experiments include *simulated traffic*, i.e., traffic generated and destined to simulated hosts, you will
need to know how to configure the transport protocol that runs beneath your applications.

Currently, our simulator supports TCP and UDP protocols; which are compatible with real implementations. In this 
section, we describe the configurable parameters of both protocols. For more detailed information about our implementation
you can read our `SVEET <https://www.primessf.net/bin/view/Public/PublicationsSveet>`_ paper.

------------------------------------------ 
TCP Protocol
------------------------------------------

The following are TCP's configurable parameters:

==================   ============  ===============   ============================
Name                 Type          Default value     Description
==================   ============  ===============   ============================
tcpCA                STRING        bic               Congestion control algorithm
mss                  INT           1448 (bytes)      Maximum segment size in bytes          
sndWndSize           INT           1e9  (bytes)      Maximum sending window size
sndBufSize           INT           1e9  (bytes)      Maximum size of sender buffer
rcvWndSize           INT           1e9  (bytes)      Maximum receiving window size
samplingInterval     FLOAT         10000 (seconds)   Interval between consecutive sampling of TCP variables
iss                  INT           0                 Initial sequence number
==================   ============  ===============   ============================

For example, let's say you want to create two hosts, one server and one client and establish a HTTP
flow between them. Also, you want the server to download at a maximum rate limited by the *sndWndSize* and
you want it it to use the *highspeed* congestion control algorithm. Additionally, you also want data packets
to be no longer than one thousand bytes. Then, your model would include something like the following:

* Create top network

.. code-block:: java

  Net top = exp.createTopNet("topnet");

* Create source and destination and configure TCP parameters

.. code-block:: java

  //source of data packets
  IHost source = top.createHost("source");
  ITCPMaster tcp_s = source.createTCPMaster();
  tcp_s.setTcpCA("highspeed");
  tcp_s.setMss(1000);
  tcp_s.setSndWndSize(10);
  IHTTPServer http_s = source.createHTTPServer();

  //destination of data packets
  IHost destination = top.createHost("destination");
  IHTTPClient http_client = destination.createHTTPClient();

* Configure the blottleneck link between both hosts

.. code-block:: java
 
   ILink bottlenek = top.createLink("bottleneck");
   bottleneck.setDelay(0.064);
   bottleneck.setbandwidth(1000000);

* Create traffic between source and destination

.. code-block:: java

  ITraffic trf = top.createTraffic();
  IHTTPTraffic http_trf = trf.createHTTPTraffic();
  ...	
  http_trf.setSrcs("{.:destination}");
  http_trf.setDsts("{.:source}");

In the code excerpt show above, *source* is configured to boot up a HTTP server and as such it generates data packets
in response to HTTP requests originated by the *destination*. The TCP protocol
beneath the HTTP layer is configured, in the server, to serve no more than 10 packets of 1000 bytes every RTT or 
78,125 bytes every second per flow. Also, the server is configured to use the *highspeed* congestion control. For a
complete example you can take a look at *TCPTester.java* under *netscript/test/java_models/* of primex's source
code.

------------------------------------------ 
UDP Protocol
------------------------------------------

Like TCP, UDP has its own configurable parameter described below.

==================   ============  ===============   ============================
Name                 Type          Default value     Description
==================   ============  ===============   ============================
max_datagram_size    INT           1470 (bytes)      maximum datagram size
==================   ============  ===============   ============================

UDP traffic is specified in the following way:

.. code-block:: java

	ITraffic t = top.createTraffic();
	IUDPTraffic tr = t.createUDPTraffic();
	tr.setInterval(10);
	tr.setBytesToSendEachInterval("1500");
	tr.setCount("2");
	tr.setSrcs("{.:host2}");
	tr.setDsts("{.:host1}");
	
In the code excerpt shown above, *host2* will send *host1* 1500 bytes every 10 seconds. *setCount* indicates
the number of intervals of length *setInterval* before the traffic ends. For a complete example you can take a
look at *UDPTester.java* under *netscript/test/java_models/* of primex's source code.


.. _architecture-jprime-label:

=================================
Configuring Traffic
=================================

The simulator comes with some common traffic types, including:

* TCP traffic (for downloading data from server to client over TCP)
* UDP traffic (for downloading data from server to client over UDP with constant bit-rate)
* Ping traffic (for sending ICMP pings)

The :jprime:`TrafficFactory <jprime::TrafficFactory>` class provides several methods to simply add TCP, UDP and Ping traffic flows. Below are some example functions to create simulated traffic:

* Create Simulated TCP flows: :jprime:`jprime::TrafficFactory::createSimulatedTCP(Mapping mapping, double startTime, ArrivalProcess arrivalProcess, double arrivalRate, int numberOfFlows, long bytesToTransfer, List<IHost> srcs, List<IHost> dsts)`:

  * The argument "mapping" is used to specify how to map traffic flows from a group of sources to a group of destinations. You are allowed to specify not only a single source and a single destination also a list of sources and a list of destinations in the argument "srcs" and "dsts". Once you have multiple sources and destinations, you can choose one of the following three ways to generate the traffic flows between souces and destinations automatically:
   * ONE2ONE: Each source communicates with one single destination.
   * ONE2MANY: Each source communicates with multiple destinations; each destination communicates with at least one source.
   * MANY2ONE: Each destination communicates with multiple sources; each source communicates with at least one destination.
   * ALL2ALL: Each source communicates with all destinations. This is the default setup for mapping. 

  * The argument "startTime" specifies the time in seconds at which this traffic action will become active. The default value of start time is set to be 0. The argumennt "arrivalProcess" specifies whether the time intervals between the traffic flows are constant or not. You have two choices for setting the arrival process:
   * CONSTANT: This is the default value.
   * EXPONENTIAL: The flow intervals are Exponential distributed. 

  * The argument "sendRate" specifies how many flow arrivals per second. If the arrival process is exponential, 1/sendRate will be the mean of the flows' inter-arrival times. 

  * The argument "numberOfFlows" indicates the total number of flows that occur between sources and destinations. The argument "bytesToTransfer" is the number of bytes to  be downloaded from the server to the client. For TCP traffic, "srcs" are clients, and "dsts" are servers. 


* Create Simualated UDP flows: :jprime:`jprime::TrafficFactory::createSimulatedUDP(Mapping mapping, double startTime, ArrivalProcess arrivalProcess, double arrivalRate, long sendRate, double sendTime, List<IHost> srcs, List<IHost> dsts)`

  * The argument "sendRate" is the number of bytes transfered from source to destination per second. For UDP flows, the traffic direction is from "srcs" to "dsts".

* Create Simulated Ping traffic: :jprime:`jprime::TrafficFactory::createSimulatedPing(Mapping mapping, double startTime, ArrivalProcess arrivalProcess, double arrivalRate, long numberOfPings, List<IHost> srcs, List<IHost> dsts)`

  * The argument "numberOfPings" is the total number of Ping packets generated by this traffic. The argument "arrivalRate" specifies the number of Pings per second. 

Below is an example to show the basic usage of :jprime:`TrafficFactory <jprime::TrafficFactory>`. You can refer to :jprime:`TrafficFactory <jprime::TrafficFactory>` for a full list of all the available methods to create simulated, emulated and hybrid traffic flows in the simulator. 

.. code-block:: java

		INet topnet = null; //this would be the real topnet
		IHost srcHost=null, dstHost=null; //just place holder -- should use real hosts/nets
		INet srcNet=null, dstNet=null;//just place holder -- should use real hosts/nets
		
		TrafficFactory tFactory = new TrafficFactory(topnet, "my_traffic");
		
		//have srcHost send 10 pings to dstHost at time 1.0
		tFactory.createSimulatedPing(1.0, 10, srcHost,dstHost);
		
		//have every host in srcNet contact one host in dstNet and download 10 MB
		//every 10 second 1 host from srcNet will be chosen to start after the traffic is started 
		tFactory.createSimulatedTCP(Mapping.ONE2ONE 2.0, ArrivalProcess.CONSTANT, 10, 1, 1024*1024*10, srcNet, dstNet);
		
		//have all hosts in srcNet contact the REAL host 131.14.1.3 and download 10 MB
		//hosts will be hosts to start using an exponential distribution with an off_time of 0.1
		tFactory.createHybridTCP(3.0, ArrivalProcess.EXPONENTIAL, 0.1, 1, 1024*1024*10, srcNet, "131.14.1.3");
		
=================================================
Running Experiments with External Hosts (Portals)
=================================================

An interesting and useful characteristic of PrimoGENI is that it enables external hosts, called portals from hereon,
to interact between each other through the simulator. Also, it is even possible for *simulated hosts* to communicate
with portals.

For an experiment to contain external hosts, you must use the
`test scripts <http://www.protogeni.net/trac/protogeni/wiki/TestScripts>`_ with a rspec that requests external hosts. If
you do not know how to do this, you can take a look at the
`ProtoGENI Environment <file:///home/geni/workspace/primex/doc/_build/html/slingshot.html#protogeni-environment>`_ section.
Once you get the *manifest*, you should input that to a newly created ProtoGENI enviroment and have it ready for
deploying experiments.

.. note:: **IMPORTANT!!!** If your java/python/xml model includes *n* portals, then your ProtoGENI enviroment should
          also contain *n* portals so that perfect matching is possible

We describe how to use portals with an example. Creating a model that includes portals is no different that creating 
a simple except for:

1. When create an interface that will connect to an portal then that interface must be marked accordingly.
 
   .. code-block:: java
 	
 	  IInterface myportal;
 	  myportal.createTrafficPortal();

2. Via portals, more networks other than the IP space of the experiment can be reachable, even the Internet. To do so
   we must explicitly enable that in the portal interface.
   
   .. code-block:: java

      myportal.addReachableNetwork("65.0.0.0");

3. The portal interface IP address must be explicitly set.

   .. code-block:: java
     
      myportal.setIpAddress("10.10.3.1");
      
   .. note:: **IMPORTANT!!!** The subnetwork connecting the portal interface inside the simulator (myportal in our case)
      to the interface of the real machine **must** be set as **reachable** as shown above or external hosts will not be
      reachable from primex.
      
 A complete example of a model using portals can be found
 `HERE <http://users.cis.fiu.edu/~meraz001/slingshotfiles/Portals.java>`_. In that example we have what we call the 
 *campus model*. In that model we have two portals configured which are signaled as triangles in the display as show
 below.
 
  .. image:: images/slingshot_portals_display.png
    :width: 5in

Now, we have to create a suitable environment to host this experiment. 

1. Follow the same steps to create a normal enviroment up to the point when you reach this popup window:

  .. image:: images/slingshot_portals_creating_env_1.png
    :width: 7in

2. Then, one node must be tagged as **master** and another as **slave** as discussed in previous sections. If you are using
   the rspec provided `HERE <http://users.cis.fiu.edu/~meraz001/slingshotfiles/utah-4nodes-datanetwork.rspec>`_, then
   the node with three interfaces must be tagged as slave. Note that the node wich are configured to use the 
   **urn:publicid:IDN+emulab.net+image+PRIME//primoGENIv2** OS image are used for being master or slaves while the
   others are automatically tagged as external hosts.
   
3. Click *Finish*. You should see the following window:

   .. image:: images/slingshot_portals_creating_env_2.png
    :width: 6in
    
   In that window the summary of the environment is shown. Notice that the number of portals is two for our environment.
   
4. Click *Finish*.

We are now in position to launch the experiment.

1. Compile de model.

2. Click on *Run Experiment*.

3. Choose the environment you just created and click *Next*.

4. Enter the *Runtime* and click *Next*.

5. Now, the portals inside the simulator have to mapped to real interfaces. Doing so in out model is easy because we 
   set explicitly our both interfaces to have the IPs *10.10.3.1* and *10.10.1.2*; which are exactly the same IPs of the
   slice we got from Emulab/ProtoGENI after using the test scripts. Therefore, we recommend to first get the slice, inspect
   the IPS of the slaves which connect to external hosts and make the corresponding interfaces in the java model to have
   the same IPs. You should see something like the following:
   
   .. image:: images/slingshot_portals_creating_env_3.png
    :width: 6in
    
   Then, you just have to choose an IP from *Portals* and the same IP from *Interfaces* and then press *Link*. Note in the
   figure above that the interface and portal 10.10.1.2 have already been linked. You can repeat this to link the portal and interface with IP 10.10.3.1.
   
6. Once there are no more portals to be linked then click *Finish*.
   
   After the experiment has already been instatiated you **must** login to the external nodes and add a route for reaching the
   IP space. You get the IPs of the portals from the manifest. For example:

   .. code-block:: console
      
      Miguel-Erazos-MacBook-Pro:~ Erazo$ ssh merazo@pc277.emulab.net
      externalnode1:~% sudo su -
      root@externalnode1:~# ifconfig
      eth0      Link encap:Ethernet  HWaddr 00:11:43:e4:39:17  
                inet addr:155.98.39.77  Bcast:155.98.39.255  Mask:255.255.252.0
          
      eth4      Link encap:Ethernet  HWaddr 00:04:23:b7:42:d0  
                inet addr:10.10.1.1  Bcast:10.10.1.255  Mask:255.255.255.0
          
      lo        Link encap:Local Loopback  
                inet addr:127.0.0.1  Mask:255.0.0.0
          
     root@externalnode1:~# ip route add 192.0.0.0/8 via 10.10.1.2 dev eth4
  
   Note from above that we add a route to 192.0.0.0/8 space because that is the IP space of the experiment and we want
   to reach it from the **private interface of the external node** which we call *experiment network*. 
   
7. After this is done, the portals can reach  **any** of other portals via the simulator or **any** other simulated host.
   Note how we can ping any host. Using *traceroute -I* displays the route from the source to the destination
   (We use *-I* because we use ICMP protocol).

   .. code-block:: console

      PING 192.1.8.26 (192.1.8.26) 56(84) bytes of data.
      64 bytes from 192.1.8.26: icmp_req=1 ttl=54 time=2.07 ms
      64 bytes from 192.1.8.26: icmp_req=2 ttl=54 time=2.06 ms

      root@externalnode1:~# traceroute -I 192.1.8.26
	  traceroute to 192.1.8.26 (192.1.8.26), 30 hops max, 60 byte packets
 	  1  192.1.3.21 (192.1.3.21)  2.422 ms  2.424 ms  2.423 ms
 	  2  192.1.3.17 (192.1.3.17)  2.422 ms  2.425 ms  2.426 ms
 	  3  192.1.8.129 (192.1.8.129)  2.424 ms  2.428 ms  2.430 ms
 	  4  192.1.8.53 (192.1.8.53)  2.429 ms  2.432 ms  2.432 ms
 	  5  192.1.8.42 (192.1.8.42)  2.431 ms  2.434 ms  2.413 ms
 	  6  192.1.8.62 (192.1.8.62)  2.410 ms  1.636 ms  1.618 ms
 	  7  192.1.8.14 (192.1.8.14)  1.731 ms  1.840 ms  1.829 ms
 	  8  192.1.8.26 (192.1.8.26)  2.003 ms  2.073 ms  2.055 ms


   We can even show in slingshot the route of one host to another inputting the traceroute output after clicking on 
   *Add Graph Overlay* as shown below.
   
   .. image:: images/slingshot_traceroute_output.png
    :width: 6in
   
   Then, slingshot will show:
   
   .. image:: images/slingshot_traceroute.png
    :width: 6in
    
   In this way, you can start/run **any application** between portals and use the simulator to mimic any enviroment within
   which these real applications would be running.
