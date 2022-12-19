wget https://mirrors.estointernet.in/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz &&
  tar -xvf apache-maven-3.6.3-bin.tar.gz &&
  mv apache-maven-3.6.3 /opt/

export M2_HOME='/opt/apache-maven-3.6.3' >> ~/.bashrc
export PATH="$M2_HOME/bin:$PATH" >> ~/.bashrc
source ~/.bashrc
