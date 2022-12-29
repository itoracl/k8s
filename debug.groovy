pipeline { 
    agent any
    environment {
        ANS_HOME = tool('ansible')
    }
    stages { 
        stage('Deploy') { 
            steps { 
            //    sh 'ansible-playbook debug.yaml'//
            //   echo $ANS_HOME
               ansiblePlaybook becomeUser: 'cloudshell', colorized: true, forks: 2, playbook: 'debug.yaml'
            }
        }
    }
}