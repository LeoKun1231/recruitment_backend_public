//git凭证Id
def git_auth = "20e203be-3c59-4f2b-97ac-c1eb84e7efa5"
//git的项目地址
def git_url = "https://gitee.com/leo3366/recruitment_backend.git"
//git拉取的分支
def git_branch="main"

node{
    stage('拉取代码'){
       checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: '20e203be-3c59-4f2b-97ac-c1eb84e7efa5', url: 'https://gitee.com/leo3366/recruitment_backend.git']]]
    }
    sh 'mvn clean package -Dmaven.test.skip=true'
    sh "mvn clean"
    sh "mvn install"

    stage('编译,安装service_user工程'){
            echo "Building service_user"
            try{
                echo "第一次启动容器service_user"
                sh "docker run  --name=service_user -d -p 8210:8210 service_user"
            }catch(Throwable e){
                echo "容器service_userr已经启动,现在终止容器运行"
                sh "docker stop service_user"
                echo "移除容器service_user"
                sh "docker rm service_user"
                echo "重新启动容器service_user"
                sh "docker run  --name=service_user -d -p 8210:8210 service_user"
                echo "清理旧镜像(状态为none的镜像)"
                sh "docker image prune -f"
            }
    }

    stage('编译,安装service_common工程'){
            echo "Building service_common"
            try{
                echo "第一次启动容器service_common"
                sh "docker run  --name=service_common -d -p 8202:8202 service_common"
            }catch(Throwable e){
                echo "容器service_common已经启动,现在终止容器运行"
                sh "docker stop service_common"
                echo "移除容器service_common"
                sh "docker rm service_common"
                echo "重新启动容器service_common"
                sh "docker run  --name=service_common -d -p 8202:8202 service_common"
                echo "清理旧镜像(状态为none的镜像)"
                sh "docker image prune -f"
            }
    }

    stage('编译,安装service_company工程'){
            echo "Building service_company"
            try{
                echo "第一次启动容器service_company"
                sh "docker run  --name=service_company -d -p 8201:8201 service_company"
            }catch(Throwable e){
                echo "容器service_company已经启动,现在终止容器运行"
                sh "docker stop service_company"
                echo "移除容器service_company"
                sh "docker rm service_company"
                echo "重新启动容器service_company"
                sh "docker run  --name=service_company -d -p 8201:8201 service_company"
                echo "清理旧镜像(状态为none的镜像)"
                sh "docker image prune -f"
            }
    }

    stage('编译,安装service_oss工程'){
            echo "Building service_oss"
            try{
                echo "第一次启动容器 service_oss"
                sh "docker run  --name=service_oss -d -p 8205:8205 service_oss"
            }catch(Throwable e){
                echo "容器 service_oss 已经启动,现在终止容器运行"
                sh "docker stop service_oss"
                echo "移除容器 service_oss"
                sh "docker rm service_oss"
                echo "重新启动容器 service_oss"
                sh "docker run  --name=service_oss -d -p 8205:8205 service_oss"
                echo "清理旧镜像(状态为none的镜像)"
                sh "docker image prune -f"
            }
    }

    stage('编译,安装 service_sms 工程'){
            echo "Building service_sms"
            try{
                echo "第一次启动容器 service_sms"
                sh "docker run  --name=service_sms -d -p 8204:8204 service_sms"
            }catch(Throwable e){
                echo "容器 service_sms 已经启动,现在终止容器运行"
                sh "docker stop service_sms"
                echo "移除容器 service_sms"
                sh "docker rm service_sms"
                echo "重新启动容器 service_sms"
                sh "docker run  --name=service_sms -d -p 8204:8204 service_sms"
                echo "清理旧镜像(状态为none的镜像)"
                sh "docker image prune -f"
            }
    }
    stage('编译,安装 gateway 工程'){
            echo "Building gateway"
            try{
                echo "第一次启动容器 gateway"
                sh "docker run  --name=gateway -d -p 8666:8666 gateway"
            }catch(Throwable e){
                echo "容器 gateway 已经启动,现在终止容器运行"
                sh "docker stop gateway"
                echo "移除容器 gateway"
                sh "docker rm gateway"
                echo "重新启动容器 gateway"
                sh "docker run  --name=gateway -d -p 8666:8666 gateway"
                echo "清理旧镜像(状态为none的镜像)"
                sh "docker image prune -f"
            }
    }

}
