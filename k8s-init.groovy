pipeline { 
    agent any
    stages { 
        stage('Deploy Kubernetes Cluster Conform to kubeadm 1.26 kubernetes.io official') { 
           parallel {
                stage('Apply system requirements')
                   steps { 
                     sh 'ansible-playbook init-phd.yaml'
                   }
                stage('Installing containerd container runtime')
                   steps{
                     sh 'ansible-playbook init-phb.yaml'
                   }
                stage('Configuring systemd cgroup driver')
                   steps{
                     sh 'ansible-playbook init-phc.yaml'
                   }
           }
          stage('Bootstrap cluster with kubeadm'){
            steps { 
                sh 'ansible-playbook init-pha.yaml'
            }
          }
        }
    }
}
