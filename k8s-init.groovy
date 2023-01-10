pipeline { 
    agent any
    options {
        parallelsAlwaysFailFast()
    }
    stages { 
        stage('Deploy Kubernetes \nCluster Conform to kubeadm 1.26 kubernetes.io official'){ 
           parallel {
             stage('Apply system requirements'){
                   steps { 
                     sh 'ansible-playbook -i files/hosts init-phd.yaml'
                   }
             }
             stage('Installing containerd container runtime'){
                   steps{
                     sh 'ansible-playbook init-phb.yaml'
                   }
             }
             stage('Configuring systemd cgroup driver'){
                   steps{
                     sh 'ansible-playbook init-phc.yaml'
                   }
             }
             stage('Installing kubeadm, kubelet and kubectl'){
                   steps { 
                     sh 'ansible-playbook init-pha.yaml'
                   }
             }
           }
        }
         stage('Bootstrap cluster with kubeadm'){
            steps { 
                sh 'ansible-playbook init.yaml'
            }
         }
        stage('Install necessary kubernetes cluster services'){ 
           parallel {
             stage('Installing flannel network overlay '){
                   steps { 
                     sh 'ansible-playbook init-ppha.yaml'
                   }
             }
             stage('Installing kubernetes dashboard recommended'){
                   steps{
                     sh 'ansible-playbook init-pphb.yaml'
                   }
             }
             stage('Installing ingress controller'){
                   steps{
                     sh 'ansible-playbook init-pphc.yaml'
                   }
             }
           }
        }
    }
}
