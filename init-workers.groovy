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
                     sh 'ansible-playbook -i files/workers init-phd.yaml'
                   }
             }
             stage('Installing containerd container runtime'){
                   steps{
                     sh 'ansible-playbook -i files/workers init-phb.yaml'
                   }
             }
             stage('Configuring systemd cgroup driver'){
                   steps{
                     sh 'ansible-playbook -i files/workers init-phc.yaml'
                   }
             }
             stage('Installing kubeadm, kubelet and kubectl'){
                   steps { 
                     sh 'ansible-playbook -i files/workers init-pha.yaml'
                   }
             }
           }
        }
         stage('Join workers with kubeadm'){
            steps { 
                sh 'ansible-playbook -i files/hosts init-workers.yaml'
            }
         }
    }
}