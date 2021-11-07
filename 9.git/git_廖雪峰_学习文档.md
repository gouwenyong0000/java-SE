
# git常用
最常用的 git 命令有：
   add        添加文件内容至索引
   bisect     通过二分查找定位引入 bug 的变更
   branch     列出、创建或删除分支
   checkout   检出一个分支或路径到工作区
   clone      克隆一个版本库到一个新目录
   commit     记录变更到版本库
   diff       显示提交之间、提交和工作区之间等的差异
   fetch      从另外一个版本库下载对象和引用
   grep       输出和模式匹配的行
   init       创建一个空的 Git 版本库或重新初始化一个已存在的版本库
   log        显示提交日志
   merge      合并两个或更多开发历史
   mv         移动或重命名一个文件、目录或符号链接
   pull       获取并合并另外的版本库或一个本地分支
   push       更新远程引用和相关的对象
   rebase     本地提交转移至更新后的上游分支中
   reset      重置当前HEAD到指定状态
   rm         从工作区和索引中删除文件
   show       显示各种类型的对象
   status     显示工作区状态
   tag        创建、列出、删除或校验一个GPG签名的 tag 对象


# 创建版本控制
+ `git init`				通过git init命令把这个目录变成Git可以管理的仓库

+ `git add <file>`			用命令git add告诉Git，把文件添加到仓库

+ `git commit -m <message>`	git commit命令，-m后面输入的是本次提交的说明，可以输入任意内容，当然最好是有意义的
+ `git commit -a`

# 时光穿梭机
+ `git status` 要随时掌握工作区的状态。
+ `git diff <file>`  如果git status告诉你有文件被修改过，用`git diff <file>`可以查看修改内容。

```shell
$ git status
On branch master
Changes not staged for commit:
  (use "git add <file>..." to update what will be committed)
  (use "git checkout -- <file>..." to discard changes in working directory)
```



## 版本回退
+ HEAD指向的版本就是当前版本，因此，Git允许我们在版本的历史之间穿梭，使用命令`git reset --hard commit_id`。

+ 穿梭前，用`git log`可以查看提交历史，以便确定要回退到哪个版本。

+ 要重返未来，用`git reflog`查看命令历史，以便确定要回到未来的哪个版本。

## 工作区和暂存区

+ 工作区（Working Directory）
    
    >就是你在电脑里能看到的目录，比如我的learngit文件夹就是一个工作区：
    
+ 版本库（Repository）
>工作区有一个隐藏目录.git，这个不算工作区，而是Git的版本库。Git的版本库里存了很多东西，其中最重要的就是称为stage（或者叫index）的暂存区，还有Git为我们自动创建的第一个分支master，以及指向master的一个指针叫HEAD。


> 前面讲了我们把文件往Git版本库里添加的时候，是分两步执行的：
>+ 第一步是用git add把文件添加进去，实际上就是把文件修改添加到暂存区；
>+ 第二步是用git commit提交更改，实际上就是把暂存区的所有内容提交到当前分支。

![工作区站存取](https://www.liaoxuefeng.com/files/attachments/919020037470528/0)

>因为我们创建Git版本库时，Git自动为我们创建了唯一一个master分支，所以，现在，git commit就是往master分支上提交更改。
>你可以简单理解为，需要提交的文件修改通通放到暂存区，然后，一次性提交暂存区的所有修改。


`git diff HEAD -- <file>`   命令可以查看工作区和版本库里面最新版本的区别
## 撤销修改


`git checkout -- <file>`  git checkout其实是用版本库里的版本替换工作区的版本，无论工作区是修改还是删除，都可以“一键还原”

+ 场景1：当你改乱了工作区某个文件的内容，想直接丢弃工作区的修改时，用命令git checkout -- file。
+ 场景2：当你不但改乱了工作区某个文件的内容，还添加到了暂存区时，想丢弃修改，分两步，第一步用命令`git reset HEAD <file>`，就回到了场景1，第二步按场景1操作。
+ 场景3：已经提交了不合适的修改到版本库时，想要撤销本次提交，参考版本回退一节，不过前提是没有推送到远程库。
## 删除文件
`git rm <file>`  用于删除一个文件。如果一个文件已经被提交到版本库，那么你永远不用担心误删，但是要小心，你只能恢复文件到最新版本，你会丢失最近一次提交后你修改的内容

>小提示：先手动删除文件，然后使用`git rm <file>`和`git add<file>`效果是一样的。

>另一种情况是删错了，因为版本库里还有呢，所以可以很轻松地把误删的文件恢复到最新版本：

 `git checkout -- <file>`
git checkout其实是用版本库里的版本替换工作区的版本，无论工作区是修改还是删除，都可以“一键还原”。

 注意：从来没有被添加到版本库就被删除的文件，是无法恢复的！

# 远程仓库
[git授权_廖雪峰](https://www.liaoxuefeng.com/wiki/896043488029600/896954117292416)

+ 第1步：创建SSH Key。在用户主目录下，看看有没有.ssh目录，如果有，再看看这个目录下有没有id_rsa和id_rsa.pub这两个文件，如果已经有了，可直接跳到下一步。如果没有，打开Shell（Windows下打开Git Bash），创建SSH Key：
 `ssh-keygen -t rsa -C "youremail@example.com"`
 你需要把邮件地址换成你自己的邮件地址，然后一路回车，使用默认值即可，由于这个Key也不是用于军事目的，所以也无需设置密码。
 如果一切顺利的话，可以在用户主目录里找到.ssh目录，里面有id_rsa和id_rsa.pub两个文件，这两个就是SSH Key的秘钥对，id_rsa是私钥，不能泄露出去，id_rsa.pub是公钥，可以放心地告诉任何人。

+第2步：登陆GitHub，打开“Account settings”，“SSH Keys”页面：
然后，点“Add SSH Key”，填上任意Title，在Key文本框里粘贴id_rsa.pub文件的内容：

> 注意: 设置仓库的邮箱地址跟创建秘钥的一致,(可以配置全局和本仓库)  git Here GUI  -> edit -> opinion进行设置



## 添加远程仓库
+ 要关联一个远程库，使用命令`git remote add origin git@github.com:gouwenyong0000/stidy_git_lxf.git`  
> git@server-name:path/repo-name.git; 是github的SSH连接地址
+ 关联后，使用命令`git push -u origin master`第一次推送master分支的所有内容；(可能需要输入密码)
+ 此后，每次本地提交后，只要有必要，就可以使用命令`git push origin master`推送最新修改；

## 删除关联仓库
`git remote rm origin`

## 克隆
要克隆一个仓库，首先必须知道仓库的地址，然后使用`git clone`命令克隆。
Git支持多种协议，包括https，但ssh协议速度最快。

# 分支管理
## 创建与合并分支

+ 查看分支：`git branch -v`
+ 创建分支：`git branch <name>`
+ 切换分支：`git checkout <name>`或者`git switch <name>`
+ 创建+切换分支：`git checkout -b <name>`或者`git switch -c <name>`
+ 合并某分支到当前分支：`git merge <name>`
+ 删除分支：`git branch -d <name>`



## 解决冲突



+ `git merge <branch_dev>`  将branch_dev 分支合并到当前分支【先切换到目标分支】
+ `git log --graph`命令可以看到分支合并图。



有如下冲突：

```
<<<<<<< HEAD:test.c
printf (“test1″);
=======
printf (“test2″);
>>>>>>> issueFix:test.c
```



可以看到 `=======` 隔开的

​		上半部分，是 HEAD（即 master 分支，在运行 merge 命令时检出的分支）中的内容，

​		下半部分是在 issueFix 分支中的内容。解决冲突的办法无非是二者选其一或者由你亲自整合到一起。

手动合并后，删除了 <<<<<<<，=======，和>>>>>>> 这些行。

在解决了所有文件里的所有冲突后

+ 运行`git add `将把它们标记为已解决（resolved）。

+ 然后使用`git commit`命令进行提交，merge就算完成了

