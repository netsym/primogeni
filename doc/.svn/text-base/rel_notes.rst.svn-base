.. meta::
   :description: PrimoGENI User's Guide
   :keywords: PrimoGENI, simulation, emulation, network simulation, network emulation, PRIME, PRIME, SSFNet


--------------------------
Version 1.0.1
--------------------------

Below are known issues. If you experience problems that are not documented here please contact :literal:`help <at> primessf <dot> net` so we can fix it.

* Multiple failures occur if you use more than 1 CPU per compute node.

  * There is no workaround for this bug. This issue is actively being worked and will be resolved soon.
  * When executing local experiments only use a single CPU until this issue is resolved.
  * This issue does not effect cluster or PrimoGENI environments.

* When an experiment running on the local machine is manually terminated, Slingshot may fail to terminate the simulation process.

  * The workaround is to use a terminal and look for the simulation process via :samp:`ps aux | grep prime` and manually kill it. 
  * We are actively working to resolve this issue.

* Applications may fail to properly start on emulated hosts/routers. 

  * There is no workaround for this bug. This issue is actively being worked and will be resolved soon.

* User's cannot specify their own PrimoGENI OS image. 

  * The workaround is to edit src/monitor/provisioners/RSpec.java and change :literal:`primoGENIv2-PG` to your custom image.
  * We are working to add this option to the PrimoGENI environment. 

* If you start multiple instances of the Slingshot the successive instances will fail to start.

  * This is expected behavior. The issue is that Slingshot's database can only be opened by a single process at a time. 
  * We are working to provide a better error message.

