pipeline { 
    agent any
    stages { 
        stage('Deploy') { 
            steps { 
                sh 'ansible-playbook debug1.yaml'//
            //   echo $ANS_HOME
            //   ansiblePlaybook becomeUser: 'cloudshell', colorized: true, forks: 2, playbook: 'debug.yaml'
            }
        }
    }
}