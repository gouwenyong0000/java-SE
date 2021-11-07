# 一、删除文件

1. 克隆远程仓库到本地库。

   例如使用 ssh 方法：`git clone git@github.com:xxx/xxx.git`

2. 对需要删除的文件、文件夹进行如下操作:

   `git rm test.txt (删除文件)`

   `git rm -r test (删除文件夹)`

3. 提交修改

   `git commit -m "Delete some files."`

4. 将修改提交到远程仓库的 xxx 分支:

   `git push origin xxx`

# 二、 删除远程仓库 但不删本地资源

我们在使用 idea 开发的过程中经常会出现新建项目的时候直接把 xxx.iml 文件也添加到了 git trace

当然这并不会出现什么问题，问题是当我们把 xxx.iml 文件 push 到我们 github 上之后，然后在另一台电脑上 pull 了下来会出现一些问题，因为 xxx.iml 文件不是项目的源码。也就是说对于导入项目来说是多余的。

```
正规的源码目录：
　　|src/
　　|pom.xml
　　|.ignore
```

但是，我们又不能直接在本地删除 xxx.iml。因为该文件是我们在本地开发的时候必须的。

那么问题来了：我们要在保留本地文件的情况下，删除远程仓库的文件（程序员一定要通过技术手段来实现目的，捂脸笑）

ok, 废话不多说，下面是解决方案：

　　把 xxx.iml 加到 \`.gitignore\` 里面忽略掉，然后提交使. gitignore 生效，也既是

```sh
git rm -r --cached xxx.iml　　#-r 是递归的意思   当最后面是文件夹的时候有用    --cached 保留本地文件 删除远程仓库的文件
（git add xxx.iml）　　　　　  # 若. gitignore 文件中已经忽略了 xxx.iml 则可以不用执行此句
git commit -m "ignore xxx.xml"
git push

#当我们需要删除暂存区或分支上的文件, 同时工作区也不需要这个文件了, 可以使用
git rm file_path
git commit -m 'delete somefile'
git push

#当我们需要删除暂存区或分支上的文件, 但本地又需要使用, 只是不希望这个文件被版本控制, 可以使用
git rm --cached file_path
git commit -m 'delete remote somefile'
git push
```



# 三、本地仓库更换绑定的远程仓库

#### 方法一 通过命令直接修改远程地址

1.  进入 git\_test 根目录
2.  `git remote` 查看所有远程仓库， git remote xxx 查看指定远程仓库地址
3.  `git remote set-url origin http://192.168.100.235:9797/john/git_test.git`

#### 方法二 通过命令先删除再添加远程仓库

1.  进入 git\_test 根目录
2.  git remote 查看所有远程仓库， git remote xxx 查看指定远程仓库地址
3.  git remote rm origin
4.  git remote add origin [http://192.168.100.235:9797/john/git\_test.git](http://192.168.100.235:9797/john/git_test.git)

#### 方法三 直接修改配置文件

1.  进入 git\_test/.git
2.  vim config   
    `  
    [core]   
    repositoryformatversion = 0   
    filemode = true   
    logallrefupdates = true   
    precomposeunicode = true   
    [remote "origin"]   
    url = http://192.168.100.235:9797/shimanqiang/assistant.git   
    fetch = +refs/heads/*:refs/remotes/origin/*   
    [branch "master"]   
    remote = origin   
    merge = refs/heads/master`
    
    修改 \[remote “origin”\] 下面的 url 即可
    

#### 方法四 通过第三方 git 客户端修改。

以 SourceTree 为例，点击 仓库 -> 仓库配置 -> 远程仓库 即可管理此项目中配置的所有远程仓库， 而且这个界面最下方还可以点击编辑配置文件，同样可以完成方法三。