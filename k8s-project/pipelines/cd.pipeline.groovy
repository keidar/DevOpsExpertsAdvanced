#!groovy
@Library('jenkins-shared-lib@main') _
pipeline {
	agent {
    kubernetes {
      yaml '''
        apiVersion: v1
        kind: Pod
        spec:
          containers:
          - name: helm
            image: alpine/k8s:1.22.10
            command:
            - cat
            tty: true
        '''
    }
  }
	stages {
    	stage('CD Pipeline') {
    		steps {
        			git branch: 'master', changelog: false, poll: false, url: 'https://github.com/keidar/DevOpsExpertsAdvanced.git'
          container('helm'){
            script{
              dir("k8s-project/helm_charts"){
                  sh " helm upgrade --install producer producer"
                  sh " helm upgrade --install consumer consumer"
              }           
            }
          }
    	}
    }  
  }
}

