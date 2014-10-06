#!/bin/sh

echo -e "\n*********************************************\nStarting Primogeni Installation\n*********************************************\n"
false || mkdir -p ~/Downloads
#false || { mkdir -p ~/Downloads  && cd ~/Downloads;} || cd ~/Downloads;
#mkdir -p ~/Downloads  && cd $_
#Downloading java from oracle
#wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/7u4-b20/jdk-7u4-linux-x64.tar.gz"
echo -e "\n*********************************************\nDownloading oracle java\n*********************************************\n"
wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/6u23-b05/jdk-6u23-linux-x64.bin"
#https://ivan-site.com/2012/05/download-oracle-java-jre-jdk-using-a-script/
#wget -O ~/Downloads/ --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/6u23-b05/jdk-6u23-linux-x64.bin"
#change permission for jdk folder
#if [ ! -f ~/Downloads/jdk-6u23-linux-x64.bin]; then
if [ ! -f ./jdk-6u23-linux-x64.bin ]; then
    echo -e "\n*********************************************\nJava BIN File not found in ~/Downloads!\n*********************************************\n"
else
    echo -e "\n*********************************************\nUnzipping java and moving to /usr/local/jdk*\n*********************************************\n"
    
    chmod +x ./jdk*
    ./jdk-6u23-linux-x64.bin
    mkdir -p /usr/local/
    mv jdk1.6.0_23 /usr/local/
    #chmod +x ~/Downloads/jdk*
    #~/Downloads/jdk-6u23-linux-x64.bin    
    #mkdir -p /usr/local/
    #mv ~/Downloads/jdk1.6.0_23 /usr/local/

    #RHEL- Centos only setenv
    echo "export PATH=\$PATH:/usr/local/jdk1.6.0_23/bin" >> /etc/rc.d/rc.local
    export PATH=$PATH:/usr/local/jdk1.6.0_23/bin
    echo "export JAVA_HOME=/usr/local/jdk1.6.0_23/" >> /etc/rc.d/rc.local
    export JAVA_HOME=/usr/local/jdk1.6.0_23
fi
#ASK question if want to delete java.bin file
#rm -rf ~/Downloads/jdk-6u23-linux-x64.bin
echo -e "\n*********************************************\nFinished setting up JAVA.\nInstalling: gcc gcc-c++ ant subversion bison flex libpcap-devel libnet mpich mpi!\n*********************************************\n"
export JAVA_HOME=/usr/local/jdk1.6.0_23
#yum update
yum install gcc gcc-c++ ant subversion bison flex libpcap-devel libnet git
#yum install mpi mpich2 
#C Compliler (gcc) needs to be in the system before trying to compile hydra and mpich
#!/bin/sh
echo -e "\n*********************************************\nDownloading hydra and mpich (v1.3.2p1)!, and Unzippping\n*********************************************\n"
wget -O ~/Downloads/hydra-1.3.2p1.tar.gz http://www.mpich.org/static/tarballs/1.3.2p1/hydra-1.3.2p1.tar.gz
wget -O ~/Downloads/mpich2-1.3.1.tar.gz http://www.mpich.org/static/tarballs/1.3.1/mpich2-1.3.1.tar.gz
#wget -O ~/Downloads/mpich2-1.3.2p1.tar.gz http://www.mpich.org/static/downloads/1.3.2p1/mpich2-1.3.2p1.tar.gz
for i in ~/Downloads/*.tar.gz; do tar xvzf $i -C ~/Downloads/; done

echo -e "\n*********************************************\Installing Hydra and Mpich2\n*********************************************\n"

cd /root/Downloads/hydra-1.3.2p1
./configure
make clean
make distclean
./configure
make 
make install
cd /root/Downloads/mpich2-1.3.1
./configure --disable-f77 --disable-fc --prefix=/usr/mpich
make
make install prefix=/usr/mpich
# Thanks: http://stackoverflow.com/questions/11307465/destdir-and-prefix-of-make
#Working mpich2 setup
#  233  cd /root/Downloads/mpich2-1.3.1
#  234  ./configure --disable-f77 --disable-fc --prefix=/usr/mpich
#  236  make
#  237  sudo make install prefix=/usr/mpich

echo -e "\n*********************************************\nStarting Primex installation\n*********************************************\n"
mkdir -p /primex
git clone https://github.com/netsym/primogeni
mv primogeni /primex/
#If creating image
cd /primex/primogeni/
sh /primex/primogeni/clean_build_all.sh

#else Local install.

echo -e "\n*********************************************\nInstallation COmplete\n*********************************************\n"
#/primex/primogeni/netsim/configure
#cd /primex/primogeni/netsim/
#make distclean -C /primex/primogeni/netsim/
#make clean -C /primex/primogeni/netsim/
#/primex/primogeni/netsim/configure
#make clean
#cd /primex/primogeni/netscript
#ant cleanf
#cd /primex/primogeni/topology
#make clean
#/primex/primogeni/netsim/configure
#cd /primex/primogeni/netsim/
#make ssfnet-jprime
#make ssfnet
#cd /primex/primogeni/netscript/
#ant jar
#cd /primex/primogeni/topology
#make all

#########################################################After making primex delete netscript and netIDE folder#########################################################################
# rm -rf /primex/primogeni/netIDE
# rm -rf /primex/primogeni/netscript
# rm -rf /primex/primogeni/topology

#If you do ls jprime.jar -> primogeni/netscript/dist/jprime.jar

#######################################################################Making changes to rc.local for centos#################################################################
#echo -e "export PS1=(whoami)@(hostname):(pwd)#"  >> /etc/rc.d/rc.local
#!/bin/sh
export PS1='$(whoami)@$(hostname):$(pwd) # '
echo "modprobe tun" >> /etc/rc.d/rc.local
echo "mknod /dev/net/tun c 10 200" >> /etc/rc.d/rc.local
echo "modprobe fuse" >> /etc/rc.d/rc.local
echo "mknod /dev/fuse c 10 229" >> /etc/rc.d/rc.local
echo "export PATH=/usr/kerberos/sbin:/usr/kerberos/bin:/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin:/usr/local/jdk1.6.0_23/bin:/root/bin" >> /etc/rc.d/rc.local
echo "echo 1 > /proc/sys/net/ipv4/ip_no_pmtu_disc" >> /etc/rc.d/rc.local
# perl /primex/scripts/linkExpFolder.pl
echo "sh /primex/scripts/monitor.sh &" >> /etc/rc.d/rc.local
echo "echo 0 > /proc/sys/net/ipv4/ip_forward" >> /etc/rc.d/rc.local
echo "echo Done" >> /etc/rc.d/rc.local







##PART2

#######################################################################installing OpenVZ#######################################################################
###################################################################################
#http://www.howtoforge.com/installing-and-using-openvz-on-centos-5.7
####################################################################################
cd /etc/yum.repos.d
wget http://download.openvz.org/openvz.repo
rpm --import http://download.openvz.org/RPM-GPG-Key-OpenVZ
#nano openvz.repo
#yumupdate
yum install vzkernel ovzkernel
#yum install ovzkernel.x86_64
#yum install ovzkernel.i686
yum install vzctl vzquota
#Not needed for centos 6.0/6.4/6.5.. and disable the [openvz-kernel-rhel6] repository (enabled=0) and enable the [openvz-kernel-rhel5] repository instead (enabled=1): 
#yum search vzkernel 
#OR, yum search ovzkernel
#yum install vzkernel.x86_64 
#OR, yum install vzkernel.x86_64 
#nano /boot/grub/menu.lst
#The first kernel stanza should now contain the new OpenVZ kernel. Make sure that the value of default is 0 
#yum install vzctl vzquota
#     * nano /etc/sysctl.conf and set the followings:
#      ** net.ipv4.ip_forward = 1
#      ** net.ipv4.conf.default.proxy_arp = 0
#      ** net.ipv4.conf.all.rp_filter = 1
#      ** kernel.sysrq = 1
#      * net.ipv4.conf.default.send_redirects = 1
#      * net.ipv4.conf.all.send_redirects = 0
#      * net.ipv4.icmp_echo_ignore_broadcasts=1
#      * net.ipv4.conf.default.forwarding=1

#if grep -Fxq "net.ipv4.ip_forward = 0" /etc/sysctl.conf
#then 
#    sed -i 's/net.ipv4.ip_forward = 0/net.ipv4.ip_forward = 1/' /etc/sysctl.conf
#    echo "Changed"
#else 
#  if grep -Fxq "net.ipv4.ip_forward = 1" /etc/sysctl.conf
#  then
#    echo "Already there"
#  else
#    echo "Inserted net.ipv4.ip_forward"
#    echo -e "net.ipv4.ip_forward = 1\n" >> /etc/sysctl.conf
#  fi
#fi
if grep -Fxq "net.ipv4.ip_forward = 0" /etc/sysctl.conf; then      sed -i 's/net.ipv4.ip_forward = 0/net.ipv4.ip_forward = 1/' /etc/sysctl.conf;     echo "Changed"; else    if grep -Fxq "net.ipv4.ip_forward = 1" /etc/sysctl.conf;   then     echo "Already there";   else     echo "Inserted net.ipv4.ip_forward";     echo -e "net.ipv4.ip_forward = 1\n" >> /etc/sysctl.conf;   fi; fi
if grep -Fxq "kernel.sysrq = 0" /etc/sysctl.conf; then      sed -i 's/kernel.sysrq = 0/kernel.sysrq = 1/' /etc/sysctl.conf;     echo "Changed"; else    if grep -Fxq "kernel.sysrq = 1" /etc/sysctl.conf;   then     echo "Already there";   else     echo "Inserted kernel.sysrq";     echo -e "kernel.sysrq = 1\n" >> /etc/sysctl.conf;   fi; fi


if grep -Fxq "net.ipv4.conf.default.proxy_arp = 1" /etc/sysctl.conf; then      sed -i 's/net.ipv4.conf.default.proxy_arp = 1/net.ipv4.conf.default.proxy_arp = 0/' /etc/sysctl.conf;     echo "Changed"; else    if grep -Fxq "net.ipv4.conf.default.proxy_arp = 0" /etc/sysctl.conf;   then     echo "Already there";   else     echo "Inserted net.ipv4.conf.default.proxy_arp";     echo -e "net.ipv4.conf.default.proxy_arp = 0\n" >> /etc/sysctl.conf;   fi; fi
if grep -Fxq "net.ipv4.conf.all.send_redirects = 1" /etc/sysctl.conf; then      sed -i 's/net.ipv4.conf.all.send_redirects = 1/net.ipv4.conf.all.send_redirects = 0/' /etc/sysctl.conf;     echo "Changed"; else    if grep -Fxq "net.ipv4.conf.all.send_redirects = 0" /etc/sysctl.conf;   then     echo "Already there";   else     echo "net.ipv4.conf.all.send_redirects";     echo -e "net.ipv4.conf.all.send_redirects = 0\n" >> /etc/sysctl.conf;   fi; fi


if grep -Fxq "net.ipv4.conf.all.rp_filter = 0" /etc/sysctl.conf; then      sed -i 's/net.ipv4.conf.all.rp_filter = 0/net.ipv4.conf.all.rp_filter = 1/' /etc/sysctl.conf;     echo "Changed"; else    if grep -Fxq "net.ipv4.conf.all.rp_filter = 1" /etc/sysctl.conf;   then     echo "Already there";   else     echo "Inserted net.ipv4.conf.all.rp_filter";     echo -e "net.ipv4.conf.all.rp_filter = 1\n" >> /etc/sysctl.conf;   fi; fi
if grep -Fxq "net.ipv4.conf.default.send_redirects = 0" /etc/sysctl.conf; then      sed -i 's/net.ipv4.conf.default.send_redirects = 0/net.ipv4.conf.default.send_redirects = 1/' /etc/sysctl.conf;     echo "Changed"; else    if grep -Fxq "net.ipv4.conf.default.send_redirects = 1" /etc/sysctl.conf;   then     echo "Already there";   else     echo "Inserted net.ipv4.conf.default.send_redirects";     echo -e "net.ipv4.conf.default.send_redirects = 1\n" >> /etc/sysctl.conf;   fi; fi
if grep -Fxq "net.ipv4.icmp_echo_ignore_broadcasts = 0" /etc/sysctl.conf; then      sed -i 's/net.ipv4.icmp_echo_ignore_broadcasts = 0/net.ipv4.icmp_echo_ignore_broadcasts = 1/' /etc/sysctl.conf;     echo "Changed"; else    if grep -Fxq "net.ipv4.icmp_echo_ignore_broadcasts = 1" /etc/sysctl.conf;   then     echo "Already there";   else     echo "Inserted net.ipv4.icmp_echo_ignore_broadcasts";     echo -e "net.ipv4.icmp_echo_ignore_broadcasts = 1\n" >> /etc/sysctl.conf;   fi; fi
if grep -Fxq "net.ipv4.conf.default.forwarding = 0" /etc/sysctl.conf; then      sed -i 's/net.ipv4.conf.default.forwarding = 0/net.ipv4.conf.default.forwarding = 1/' /etc/sysctl.conf;     echo "Changed"; else    if grep -Fxq "net.ipv4.conf.default.forwarding = 1" /etc/sysctl.conf;   then     echo "Already there";   else     echo "Inserted net.ipv4.conf.default.forwarding";     echo -e "net.ipv4.conf.default.forwarding = 1\n" >> /etc/sysctl.conf;   fi; fi
#Verify changes
sysctl -p
#OR One string push all:
#echo -e "net.ipv4.conf.default.proxy_arp = 0\nnet.ipv4.conf.all.rp_filter = 1\nnet.ipv4.conf.default.send_redirects = 1\n net.ipv4.conf.all.send_redirects = 0\nnet.ipv4.icmp_echo_ignore_broadcasts=1\nnet.ipv4.conf.default.forwarding=1" >> /etc/sysctl.conf

#   * nano /etc/vz/vz.conf
#      * NEIGHBOUR_DEVS=all
#   * nano /etc/sysconfig/selinux
#      * SELINUX=disabled
#      * SELINUXTYPE=targeted

#-+ Very important is to copy Openvz Template files from PrimoGeniv2 image to the new image.
if grep -Fxq "SELINUX=0" /etc/sysconfig/selinux; then      sed -i 's/SELINUX=0/#SELINUX=0/' /etc/sysconfig/selinux;  echo -e "SELINUX=disabled\n" >> /etc/sysconfig/selinux; else    if grep -Fxq "SELINUX=disabled" /etc/sysconfig/selinux; then echo "already there" ; else sed -i 's/SELINUX=/#SELINUX=/' /etc/sysconfig/selinux;  echo -e "SELINUX=disabled\n" >> /etc/sysconfig/selinux; echo "Did it";  fi;  fi
if grep -Fxq "SELINUXTYPE=permissive" /etc/sysconfig/selinux; then      sed -i 's/SELINUXTYPE=permissive/#SELINUXTYPE=permissive/' /etc/sysconfig/selinux;  echo -e "SELINUXTYPE=targeted\n" >> /etc/sysconfig/selinux; else    if grep -Fxq "SELINUXTYPE=targeted" /etc/sysconfig/selinux; then echo "already there" ; else sed -i 's/SELINUXTYPE=/#SELINUXTYPE=/' /etc/sysconfig/selinux;  echo -e "SELINUXTYPE=targeted\n" >> /etc/sysconfig/selinux; echo "Did it";  fi;  fi
if grep -Fq "#NEIGHBOUR_DEVS=detect" /etc/vz/vz.conf; then      sed -i 's/#NEIGHBOUR_DEVS=detect/NEIGHBOUR_DEVS=all/' /etc/vz/vz.conf;     echo "Changed"; else    if grep -Fxq "NEIGHBOUR_DEVS=all" /etc/vz/vz.conf;   then     echo "Already there";   else     echo "Inserted NEIGHBOUR_DEVS=all";     echo -e "NEIGHBOUR_DEVS=all\n" >> /etc/vz/vz.conf;   fi; fi



#Changing ploop to simfs
if grep -Fq "VE_LAYOUT=ploop" /etc/vz/vz.conf; then      sed -i 's/VE_LAYOUT=ploop/VE_LAYOUT=simfs/' /etc/vz/vz.conf;     echo "Changed"; else    if grep -Fxq "VE_LAYOUT=simfs" /etc/vz/vz.conf;   then     echo "Already there";   else     echo "VE_LAYOUT=simfs";     echo -e "VE_LAYOUT=simfs\n" >> /etc/vz/vz.conf;   fi; fi

#Other vz template settings from previous OS image



echo -e "\n*********************************************\nOPENVZ Installation COmplete\nPlease Reboot and do a uname -r to see whatkernel the machine booted from. If its a Openvzkernel then u will see something like xxxxxxxxstabxxxxxx and do a vzlist to see if you can see that there is no container. \n*********************************************\n"


#updating primex with new monitor_scripts //auto updated.

#Downloading openvz template

wget -O /vz/template/cache/centos-6-x86_64-minimal.tar.gz http://users.cs.fiu.edu/~mobai001/primogeni_container_template/centos-6-x86_64-minimal.tar.gz
echo "sh /primex/scripts/add_rsa_pub_key_authorized_keys.sh" >> /etc/rc.d/rc.local

#Running one time 
#If there is any remaining openvz setting to be updated.
mkdir -p /primex/exps
ln -s  /primex/primogeni/netscript/dist/jprime.jar jprime.jar
ln -s /primex/primogeni/netscript/src/monitor_scripts scripts
#GOT rid of miguels current_template folder in OS image
#copy id_dsa and id_dsa.pub to ~/.ssh/
#scp obaida@prime-server.cs.fiu.edu:~/Desktop/monitor-scripts/keys.tar.gz ~/Downloads
#for i in ~/Downloads/keys.tar.gz; do tar xvzf $i -C ~/Downloads/; done 
#mv ~/Downloads/keys/* ~/.ssh/
#/primex/
#exporting scripts to my local machine
#scp /primex/scripts/scripts_exogeni_slave.tar.gz obaida@prime-server.cs.fiu.edu:~/Desktop/monitor-scripts/exogeni_scripts/

############################################		
###############Checking primex installation#
############################################
#cd /primex/primogeni/netscript
#java -jar dist/jprime.jar create foobar test/java_models/MySecondJavaModel.java
#cd /primex/primogeni/netsim
#./primex 20 ../netscript/foobar_part_1.tlv

#Restart Machine and check openvz

echo "10.10.1.1   master sliver1 geni1 primogeni1 pgc1 pgcmaster" >> /etc/hosts
echo "10.10.1.2   slave sliver2 geni2 primogeni2 pgc2  pgcslave" >> /etc/hosts


#modified copytlv script to write output to pgc_debug_msg

#mkdir -p /primex/exps

#UPDATE jprime
#killall sh
#killall java
#scp obaida@prime-server.cs.fiu.edu:~/Desktop/pgc2vega/primex/netscript/dist/jprime.jar /primex/primogeni/netscript/dist/
#sh /primex/scripts/monitor.sh &

#solved bug with not running for the first time

#Running experiment

#Creating exogeni OS image

#vzctl exec 76 ifconfig eth0
#vzctl exec 76 iperf -s &
#vzctl enter 237
#vzctl enter 
