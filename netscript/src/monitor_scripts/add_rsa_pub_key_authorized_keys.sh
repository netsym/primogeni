#!/bin/bash
total_occurance=$(grep -c "primexjprime" ~/.ssh/authorized_keys)
#echo  $total_occurance
if [ $total_occurance -gt 0 ]
then
    	echo "Already added there"
else
    	echo "Inserting key"
        echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDEN9maXJR1QuUSR02D+lnbcjVs1GohGw89yqizY8Ub/8zwR7BM6wqk8GKfDdZSY92jJNH0ru8ZXLwlLpEYfRnqbGKailQYJRCwuYiMXbz+uBv1Ae9hznXTllSwWPi4jtENRBh/ykvoFE/lMAFRPKfZ5ADlE9s3KTKj9/rJX5lO8zEk8TANZDkbuMZcs4hnmWq3noCMHaS84Z3BEFZm9qxTWaWqtQJIJK9wubz0qkGSQYV02ck9hhGu2y/Evyu+iqXcXY0QEZEJu4ojxj9xUFArYVIE0VAGk6GJ+q0Xa86h5fF4bxjOe+WX/vtu7TPfyVbNu0KFJMMk5Ep4VNddnNiz primexjprime" >> ~/.ssh/authorized_keys
        echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDEN9maXJR1QuUSR02D+lnbcjVs1GohGw89yqizY8Ub/8zwR7BM6wqk8GKfDdZSY92jJNH0ru8ZXLwlLpEYfRnqbGKailQYJRCwuYiMXbz+uBv1Ae9hznXTllSwWPi4jtENRBh/ykvoFE/lMAFRPKfZ5ADlE9s3KTKj9/rJX5lO8zEk8TANZDkbuMZcs4hnmWq3noCMHaS84Z3BEFZm9qxTWaWqtQJIJK9wubz0qkGSQYV02ck9hhGu2y/Evyu+iqXcXY0QEZEJu4ojxj9xUFArYVIE0VAGk6GJ+q0Xa86h5fF4bxjOe+WX/vtu7TPfyVbNu0KFJMMk5Ep4VNddnNiz primexjprime" >> ~/.ssh/authorized_keys2
fi
echo "DONE"

file_list=$(find  /home/*/.ssh/* -maxdepth 1 -name 'auth*')

for a_file in $file_list; do
  echo "$a_file";
  total_occurance=$(grep -c "primexjprime" $a_file)
  echo  "Keys for primexjprime:$total_occurance";
  if [ $total_occurance -gt 0 ]
  then
    echo "Already added there"
  else
    if [ `echo $a_file | grep -c "geniuser" ` -gt 0 ]
    then
      echo "Not aadding key for geniuser"
    else
      echo "Adding key for this user in:$a_file";
      echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDEN9maXJR1QuUSR02D+lnbcjVs1GohGw89yqizY8Ub/8zwR7BM6wqk8GKfDdZSY92jJNH0ru8ZXLwlLpEYfRnqbGKailQYJRCwuYiMXbz+uBv1Ae9hznXTllSwWPi4jtENRBh/ykvoFE/lMAFRPKfZ5ADlE9s3KTKj9/rJX5lO8zEk8TANZDkbuMZcs4hnmWq3noCMHaS84Z3BEFZm9qxTWaWqtQJIJK9wubz0qkGSQYV02ck9hhGu2y/Evyu+iqXcXY0QEZEJu4ojxj9xUFArYVIE0VAGk6GJ+q0Xa86h5fF4bxjOe+WX/vtu7TPfyVbNu0KFJMMk5Ep4VNddnNiz primexjprime" >> $a_file
    fi
  fi
  echo "DONE"
done

#Adding id_dsa key for mpiexec
total_occurance=$(grep -c "prime.emulab.net" ~/.ssh/authorized_keys)
#echo  $total_occurance
if [ $total_occurance -gt 0 ]
then
    	echo "MPIKEY: Already added there"
else
    	echo "Inserting key for keyless ssh for mpiexec"
	cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys
        cat ~/.ssh/id_dsa.pub >> ~/.ssh/authorized_keys2

fi
echo "DONE2"



file_list2=$(find  /home/*/.ssh/* -maxdepth 1 -name 'auth*')
echo "DONE3"

for a_file2 in $file_list2; do
  echo "DONE4"
  echo "$a_file2"
  total_occurance=$(grep -c "prime.emulab.net" $a_file2)
  echo  "Keys for prime.emulab.net:$total_occurance";
  if [ $total_occurance -gt 0 ]
  then
    echo "MPIKEY Already added there"
  else
    if [ `echo $a_file2 | grep -c "geniuser" ` -gt 0 ]
    then
      echo "MPIKEY: Not aadding MPI key for geniuser"
    else
      echo "MPIKEY: Adding key for this user in:$a_file2"
      cat ~/.ssh/id_dsa.pub >> $a_file2
    fi
  fi
done
echo "SUCCESSFULLY UPDATED ALL KEYS"
chmod 0700 ~/.ssh/id_dsa.pub

service sshd restart
echo "Boot Complete"
