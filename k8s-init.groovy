pipeline { 
    agent any
    options {
        parallelsAlwaysFailFast()
    }
    stages { 
        stage('Deploy Kubernetes Cluster conforms to kubeadm 1.26 kubernetes.io official'){ 
           parallel {
             stage('Apply system requirements'){
                   steps { 
                     sh 'ansible-playbook -i files/hosts init-phd.yaml'
                   }
             }
             stage('Installing containerd container runtime'){
                   steps{
                     sh 'ansible-playbook -i files/hosts init-phb.yaml'
                   }
             }
             stage('Configuring systemd cgroup driver'){
                   steps{
                     sh 'ansible-playbook -i files/hosts init-phc.yaml'
                   }
             }
             stage('Installing kubeadm, kubelet and kubectl'){
                   steps { 
                     sh 'ansible-playbook -i files/hosts init-pha.yaml'
                   }
             }
           }
        }
         stage('Bootstrap cluster with kubeadm'){
            steps { 
                sh 'ansible-playbook -i files/hosts init.yaml'
            }
         }
        stage('Install necessary kubernetes cluster services'){ 
           parallel {
             stage('Installing flannel network overlay '){
                   steps { 
                     sh 'ansible-playbook -i files/hosts init-ppha.yaml'
                   }
             }
             stage('Installing kubernetes dashboard recommended, ingress nginx controller'){
                   steps{
                     sh 'ansible-playbook -i files/hosts init-pphb.yaml'
                   }
             }
           }
        }
    }
}
