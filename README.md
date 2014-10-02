primogeni
=========

PGC/Slingshot Installation Guide

This document includes steps for installing slingshot (primogeni's integrated development environment) on client machines.
So far, we have successfully tested slingshot on the following machines:
Linux Ubuntu 12.04 32-bit
Mac OS 10.9 (maverick)
(more later)
Required Software

You need to make sure your machine has the following software. You can use package managers, such as "apt-get" (on Ubuntu Linux) or "macport" (on Mac OS) to install the needed software. Before start, you probably want to update/upgrade your currently installed software to the latest version (using "=sudo apt-get update; sudo apt-get upgrade=" on Ubuntu Linux, or "=sudo port selfupdate; sudo port upgrade outdated=" on Mac.
You need to make sure your machine has the basic development tools. On Mac OS, you need to install "xcode". On Ubuntu Linux, you need to install "build-essential". These development tools include the gnu compilers, makefile, and standard c library. You may also need to install tools, such as autoconf, automake, but most likely they may have already been pre-installed.
You will also find it more convenient to have the following tools:
subversion (for obtaining software from our code repository)
ssh (for secure login)
python (at least 2.6)
flex (a lexical analyzer generator used by the simulator)
bison (a parser generator used by the simulator)
ant (for building java projects, it's called apache-ant in macport)
You can do this by running 'sudo apt-get install <the-package-you-want-to-install>'. sudo apt-get install python flex bison ant ssh subversion mpi
Running these commands will either prompt you to install the package, update the package, or do nothing because you already have it.
You need to install Java. We recommend Java Oracle from their website but you can however use open jdk under Ubuntu by running 'sudo apt-get install default-jdk' (NOTE: if you choose to use open-jdk, you must install using default-jdk, not default-jre). You can download the Java Development Kit and install it by expanding the downloaded file in a private directory such as /usr/lib/jvm. And then set the following environment variables in your startup script (assuming you are using bash). You do this by adding the following two lines to the .bashrc file in your home directory.
export JAVA_HOME=<your-java-install-directory>
export PATH=$JAVA_HOME/bin:$PATH
It is recommended that you install java to /usr/lib/jvm.
The following software packages are optional for the local runtime environment:
libpcap-dev (development tools for packet capturing library)
mpi (message passing interface; you may choose openmpi, mpich or mpich2)
Installing and Compiling the Primex Simulator

1. Create a folder in your home directory called "geni". Inside of geni create two new directories called "workspace" and "slingshot". "workspace" will be the eclipse workspace and "slingshot" will be the slingshot workspace. (The names do not have to be like this. They can be whatever you want and wherever you want. However, we recommend the above).
2. Download slingshotforsummercamp.tgz from this link: http://users.cis.fiu.edu/~meraz001/slingshotforgec14/. You can also download primexforsummercamp.tgz from that site but it is recommended you use the primex from our svn repository. To do this, make sure you have svn installed and configured. Under a terminal window, cd to ~/Downloads and then run 'svn co https://svn.primessf.net/repos/primex/trunk'. This will download the latest version of primex to the directory you were when you ran this command.
3. Extract the tarball(s) wherever you want. We left them in the downloads directory. If you did svn for primex, you do not need to extract. You should however change the name of the trunk directory to primex for convenience.
If you're using primexforsummercamp.tgz, it is necessary to change some of the files. Navigate to the primex directory you extracted earlier. Navigate to netsim/src/ssf/api. Open ssftype.h with your favorite editor. You need to comment out 4 lines by inserting "//" in front of them. The line numbers are 167, 215, 243, and 291. Assure this is correct because the lines you commented out should say "typdef ptrdiff_t difference_type". Save the file and close.
Most modern Ubuntu distros do not require the following step. However, if you want to be sure, check this out anyway. Navigate to /usr/include/c++/4.6/i686-linux-gnu/bits. In here you need to open up gthr-default.h as root. You can use type sudo gedit gthr-default.h in a terminal window. In the terminal window, you must be in the directory specified above. Once the file is open, comment out lines 244 and 245 if there is something there. If there is no code in those lines or code has already been commented out, then ignore this. Save the file and close.
6. We will now build primex but we will first clean it. You can close other terminal windows. Regardless, open up a new terminal window and navigate to the primex directory that you extracted earlier. Here you will run the following commands:
cd netsim
./configure
make distclean
cd ../netscript
ant clean
cd ../topology
make clean
We have just cleaned primex. Now to build run this:
cd ../netsim
./configure	(if this doesn't work, make sure you edited the .bashrc file correctly and that all the required packages are properly installed including g++)
make ssfnet-jprime
make ssfnet (This may take a while)
cd ../netscript
ant jar
cd ../topology
make all (This gives 4 errors at the end. Ignore this. So far, it should still work).
Creating the Slingshot Project

7. Now that you have built primex for your architecture, navigate to the slingshotforsummercamp directory you extracted earlier, via your terminal window. Run the following commands:
cd eclipse
./eclipse	(you might need to do 'sudo ./eclipse' in some cases)
You can also just run eclipse from this directory by double clicking it.
Eclipse should now open and say "Workspace in use or cannot be created, choose a different one." Just click OK and manually choose the one you created earlier in /home/geni/workspace
DO NOT close the terminal window that you opened eclipse on or this will kill eclipse.
8. Now that you have eclipse running, you need to create a new project for slingshot.
a. Click on File > New > Java Project
b. Put "Slingshot" for Project name
c. Uncheck 'Use default location'
d. Browse for a project location by clicking the 'Browse' button and navigating to the primex folder you extracted earlier. Within the primex directory, select the netIDE folder and click 'OK'.
e. Click 'Next', and then 'Finish'.
9. Now your slingshot project has been created. However, the project is using the java libraries that we built it under. You need to configure Slingshot to use the java library you compiled primex with.
10. To do this, right click on the slingshot project within eclipse in the package explorer on the left. Then click on properties. This will bring up a properties window. From this window, click on Java Build Path on the left. Click on the libraries tab on the top. Select the JRE System library which should be second to last on the libraries list. Click on edit on the right. This should bring up another window. Here, select your open jdk under execution environment if you used open jdk then click finish. If you used open-jdk, you can skip the rest of this paragraph. If you used our recommended java oracle however, click on alternate JRE. The click on installed JREs on the right. This will bring up another window. Here, click add. This brings up another window. Select Standard VM then click next. Under JRE home, click on directory and navigate to your java home directory. If you followed all the insructions, it should be /usr/lib/jvm/jdk1.7.0_51. Then click ok. This should automatically load the previous window with all the system libraries. Click on finish. Now select the new JRE under the installed JREs window. It should be jdk1.7.0_51. Click ok. In the JRE System Library window, under aternate JRE, the open jdk will still be selected. You need to change it to jdk1.7.0_51, then click finish. Now your Java Build Path is correctly configured. Click ok.
11. Expand the slingshot project by clicking the arrow button next to 'Slingshot' in the package explorer on the left. Double click on the *.product file corresponding to your machine. In our case, it is 'slingshot_linux_32.product. The file should have opened up in the editor. Click on the green play button WITHIN THE PRODUCT FILE! This will build and launch slingshot. DO NOT click on the play button on the top of eclipse.
12. Slingshot will prompt you for a workspace. Just choose the one you created earlier under /home/geni/slingshot.
13. We must now configure slingshot. Click on Tools > Slingshot Config Wizard in the top menu bar. After the dialog opens up, click on browse and select the primex directory you extracted earlier. Then click 'OK' then 'Finish'.
Running an Experiment in Slingshot

14. Now we can create an experiment. This will show you the basic for creating, compiling, and running an experiment. For more detailed tutorials, refer to the slingshot manual for creating slingshot experiments and models. You can find it at this link http://users.cis.fiu.edu/~meraz001/primotutorial/getstarted.html
15. To create an experiment, click on File > New > Experiment on the top menu bar. This will bring up a new window. Name the experiment MySecondJavaModel? and Browse for the model file. Navigate to the primex directory. Go to netscript/test/java_models and select MySecondJavaModel? .java the click ok. Click finish to create the experiment. This will build a visual representation of the model and open up the model view for the project. On the right, find View Depth: and put that up to 3 by click the up arrow. Now you can see the full model. To compile the model, click on Experiment > Compile Experiment on the top menu bar. Now that the model is compiled, it cannot be changed. To run the experiment, click on Experiment > Launch Experiment on the top menu bar. This will bring up a window. Select Local Simulator then click next. For Runtime:, put 20 seconds. Leave the #Processors: at 1, and for Pace Simulation Speed:, put 1 which will indicate (Real Time). When you click finish, slingshot will begin to run your experiment. You can see this under View Depth. the Time is moving. When it gets to 10 seconds, you will see the nodes start blinking and changing colors. When the experiment terminates, a window will indicate this. Click ok. If running the experiment did not work, verify that you have configured slingshot to point to the appropriate primex directory you downloaded or checked out with svn (refer to step 13).
16. If you had any other problems, you can try following these steps again or you can contact us at <support-email-here>



A hybrid network experiment testbed on GENI with physical, simulated, and emulated network components.
