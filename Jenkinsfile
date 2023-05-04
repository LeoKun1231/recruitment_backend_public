//git凭证Id
def git_auth = "3c0fc859-1440-4a65-b7c8-b3bc6519fdac"
//git的项目地址
def git_url = "https://gitee.com/leo3366/recruitment_backend.git"
//git拉取的分支
def git_branch="main"

node{
    stage('拉取代码'){
        checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: '3c0fc859-1440-4a65-b7c8-b3bc6519fdac', url: 'https://gitee.com/leo3366/recruitment_backend.git']])
    }
}
