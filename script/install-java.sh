apt update && apt upgrade -y && apt install wget
wget https://download.java.net/openjdk/jdk8u42/ri/openjdk-8u42-b03-linux-x64-14_jul_2022.tar.gz &&
  tar -xvf openjdk-8u42-b03-linux-x64-14_jul_2022.tar.gz &&
  mv jdk1.8.0_42 /opt/

export JAVA_HOME='/opt/jdk1.8.0_42' >> ~/.bashrc
export PATH="$JAVA_HOME/bin:$PATH" >> ~/.bashrc
source ~/.bashrc