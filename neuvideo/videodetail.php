<!DOCTYPE html>
<html lang="zh-cmn-Hans" class="ua-mac ua-webkit">
<head>
<?php
        $vid = $_GET['vid'];

        include("database.php");
    
        $d= new DataBase();

        $r = $d->executeSql("select * from videos where vid=".$vid);

        $res = mysqli_fetch_assoc($r);

        $d->update("videos",array("hittimes"),array($res['hittimes']+1),"vid",$vid);

        ?>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="renderer" content="webkit">
    <meta name="referrer" content="always">
    <meta name="google-site-verification" content="ok0wCgT20tBBgo9_zat2iAcimtN4Ftf5ccsh092Xeyw" />
    <title><?php echo $res['videoname'] ?></title>
    
    <meta name="baidu-site-verification" content="cZdR4xxR7RxmM4zE" />
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Expires" content="Sun, 6 Mar 2005 01:00:00 GMT">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link rel="stylesheet" href="http://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
    <script src="http://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <script src="assets/js/star.js"></script>
    <link rel="stylesheet" type="text/css" href="https://img3.doubanio.com/f/shire/8377b9498330a2e6f056d863987cc7a37eb4d486/css/ui/dialog.css" />
    <link rel="stylesheet" type="text/css" href="https://img3.doubanio.com/f/movie/1d829b8605b9e81435b127cbf3d16563aaa51838/css/movie/mod/reg_login_pop.css" />
    <link href="https://img3.doubanio.com/f/shire/bf61b1fa02f564a4a8f809da7c7179b883a56146/css/douban.css" rel="stylesheet" type="text/css">
    <link href="https://img3.doubanio.com/f/shire/ae3f5a3e3085968370b1fc63afcecb22d3284848/css/separation/_all.css" rel="stylesheet" type="text/css">
    <link href="https://img3.doubanio.com/f/movie/8864d3756094f5272d3c93e30ee2e324665855b0/css/movie/base/init.css" rel="stylesheet">
    <link rel="alternate" href="android-app://com.douban.movie/doubanmovie/subject/25890005/" />
    <link rel="stylesheet" href="https://img3.doubanio.com/dae/cdnlib/libs/LikeButton/1.0.5/style.min.css">
    <style type="text/css">img { max-width: 100%; }</style>
    <link rel="stylesheet" href="https://img3.doubanio.com/misc/mixed_static/3ec890df550a76f7.css">
</head>

<body>

<nav class="navbar navbar-default">
    <div class="container-fluid">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="index.php">NeuVideo</a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li ><a href="">首页 <span class="sr-only">(current)</span></a></li>
                <?php
                    $res1 = $d->selectALL('videotype');
                    $tid = $res['tid'];
                    while ($row = mysqli_fetch_assoc($res1)) {

                        if($tid==$row['tid']){
                            echo '<li class="active"><a href="cat.php?tid=' . $row['tid'] . '">' .
                            $row['typename'] . '</a></li>';
                        }else{
                            echo '<li><a href="cat.php?tid=' . $row['tid'] . '">' .
                                $row['typename'] . '</a></li>';
                        }
                    }
                ?>
            </ul>
            <!--            搜索               -->
            <form class="navbar-form navbar-left">
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="Search">
                </div>
                <button type="submit" class="btn btn-default">搜索</button>
            </form>
            <ul class="nav navbar-nav topbar-nav ml-md-auto align-items-center navbar-right"> 
                <?php if (isset($_COOKIE["username"])) {
                    echo '<li class="dropdown"><a class="dropdown-toggle" data-toggle="collapse"
                     data-target="#help-dropdown" href="#"><i class="icon-info-sign"></i>';

                    $c = $d->executeSql("select * from users where uid=" . $_COOKIE["uid"]);

                    $res1 = mysqli_fetch_assoc($c);
                    echo $res1['uname'];

                    echo '<b class="caret"></b></a> <ul id="help-dropdown" class="collapse"> 
                      <li><a onclick="modify()">修改个人设置</a></li> <li><a onclick="logout()">登出</a></li> 
                      </ul> </li>';
                } else {
                    echo "<li><a onclick='openLogin()'>登录</a></li>";
                    echo "<li><a onclick='openRegister()'>注册</a></li>";
                } ?> 
                        <!-- /.dropdown-user --> </ul>

            
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>

    <div id="wrapper">
                
    <div id="content">

    <h1>
        <span property="v:itemreviewed"><?php echo $res['videoname']?></span>
            <span class="year"> <small> 上传于: <?php echo $res['uploaddate']?> </small></span>
    </h1>

        <div class="grid-16-8 clearfix">
            
        <div class="indent clearfix">
            <div class="subjectwrap clearfix">
                <div class="subject clearfix">
                    
            <div id="mainpic" class="">
            <img src="admin/<?php echo $res['pic'] ?>" title="点击看更多海报" alt="山河故人"/>
            </div>

            <!-- 简介 -->
            <div id="info">
                <p>
                    <?php echo $res['intro'] ?>
                </p>
                </div>
        </div>

            <div id="interest_sect_level" class="clearfix">
            <p><a href="videoplay.php?vid=<?php echo $res['vid'] ?>" class="btn btn-primary btn-lg" href="#" role="button" style="color: #FFFFFF">播放</a></p>
                评价:<div class="rating" id = "rating"></div>
             
                    <textarea rows="10" cols="60" id="cmt"></textarea>

                    <button id="comment" onclick="submitComments()">提交评论</button>
             
            </div>

    </div>
        </div>

        <?php 
        $sql = "SELECT
        comments.content,
        comments.cdate,
        users.uname,
        users.pic,
        levels.score
        FROM
        users
        JOIN comments
        ON users.uid = comments.uid 
        JOIN levels
        ON comments.uid = levels.uid
        AND comments.vid = levels.vid
        where levels.vid = \"".$_GET['vid'].'"';

        $r=$d->executeSql($sql);

        ?>
        <div class="review-list  ">

<!-- todo: 循环 -->

<!-- 
    
-->
<?php 

while($res = mysqli_fetch_assoc($r)){

echo '<div>
        <div class="main review-item" id="7019675">

    <header class="main-hd">
        <a>
            <img width="24" height="24" src="admin/'. $res['pic']. '">
        </a>

        <a property="v:reviewer" class="name">'.$res['uname'].'</a>

        <b style="color:#FF4500">'. $res['score'] . '分</b>

            <span property="v:dtreviewed" class="main-meta">'.$res['cdate'].'</span>

    </header>

        <div class="main-bd">

            <div id="review_7019675_short" class="review-short" data-rid="7019675">
                <div class="short-content">
                    <!-- 回复 -->
        '.$res['content'].'
                </div>
            </div>

            <div id="review_7019675_full" class="hidden">
                <div id="review_7019675_full_content" class="full-content"></div>
            </div>

        </div>
        </div>
    </div>

        </div>';
    }
        ?>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <form class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
              aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">新增</h4>
      </div>
      <div class="modal-body">

        <div class="form-group">
          <label for="txt_departmentname">用户名</label>
          <input type="text" name="username" class="form-control" id="username"
               placeholder="用户名">

        </div>
        <div class="form-group">
          <label for="txt_parentdepartment">密码</label>
          <input type="password" name="password" class="form-control" id="password"
               placeholder="密码">
        </div>
        
        <div class="form-group" style="display:None" id="repassword_input">
          <label for="txt_parentdepartment">确认密码</label>
          <input type="password" name="repassword" class="form-control" id="repassword"
               placeholder="确认密码">
        </div>
        
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><span
              class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭
        </button>
        <button type="button" id="btn_submit" class="btn btn-primary"><span
              class="glyphicon glyphicon-floppy-disk"></span>保存
        </button>
      </div>
    </form>
  </div>
</div>

    <div class="modal fade" id="modify_dialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <form class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>

                        <?php

                        $c = $d->executeSql("select * from users where uid=" . $_COOKIE["uid"]);

                        $res = mysqli_fetch_assoc($c);

                        ?>


                    <h4 class="modal-title" id="modify_label">修改</h4>
                </div>
                <div class="modal-body">

                    <div class="form-group">
                        <label for="txt_departmentname">用户名</label>
                        <input type="text" name="username" class="form-control" id="busername"
                               placeholder="用户名" value="<?php echo $res['uname'] ?>">
                        <input hidden id="uid" value="<?php echo $res['uid'] ?>" />

                    </div>
                    <div class="form-group">
                        <label for="txt_parentdepartment">密码</label>
                        <input type="password" name="password" class="form-control" id="bpassword"
                               placeholder="密码">
                    </div>
                    <div class="form-group">
                        <label for="gender">性别</label>
                    
                        <input type="radio" name="gender" value="0" <?php if ($res['gender'] == '0') echo 'checked' ?> >男
                            &nbsp;
                        <input type="radio" name="gender" value="1" <?php if ($res['gender'] == '1') echo 'checked' ?> >女
                    </div>
                    <div class="form-group">
                        <label for="birthday">生日</label>
                        <input type="date" name="birthday" class="form-control" id="birthday" value="<?php echo $res['birthdate'] ?>">
                    </div>

                    <div class="form-group">
                        <label for="pic">头像</label>
                        <input type="file" id="xdaTanFileImg" onchange="xmTanUploadImg(this)" accept="image/*"/>
                        <img id="xmTanImg" src="<?php echo "admin/" . $res['pic'] ?>" />
                        <div id="xmTanDiv"></div>
                    </div>
                    <div class="form-group">
                        <label for="email">邮箱</label>
                        <input type="email" name="email" class="form-control" id="email"
                               placeholder="邮箱" value="<?php echo $res['email'] ?>">
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" id="delMember">
                        删除
                    </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal"><span
                                class="glyphicon glyphicon-remove" aria-hidden="true"></span>关闭
                    </button>
                    <button type="button" onclick="modify_f()" class="btn btn-primary"><span
                                class="glyphicon glyphicon-floppy-disk"></span>保存
                    </button>
                </div>
            </form>
        </div>
    </div>

</body>
<script type="text/javascript">
  
  function openLogin() {
     $("#myModalLabel").text("登录");
      $("#repassword_input").hide();
      $('#myModal').modal();
  }
  
  function openRegister() {
     $("#myModalLabel").text("注册");
    $("#repassword_input").show();
      $('#myModal').modal();
  }
  

    $(function(){
        $("#btn_submit").click(function() {
            if($("#myModalLabel").text()=="登录"){
                login();
            }else{
                register();
            }
        });
    });

  function login(){
    let _username = $("#username").val();
    let _password = $("#password").val();

    var datas = {
        username: _username,
        password: _password,
        flag:0
    };
    $.ajax({
        url: 'login.php',
        type: 'post',
        data: datas,
        dataType: 'text',
        success: function (result) {
            console.log(result);
            if (result === 'success') {
                alert('登录成功');
                window.location.reload();
            }else{
                alert("登录失败: "+result);
            }
        },
        error: function (data) {
            alert("登录失败: "+data);
        }
    })
  }
  function register(){
    console.log("register");
    let _username = $("#username").val();
    let _password = $("#password").val();
    let repassword = $("#repassword").val();

    if(_password!=repassword){
      alert('密码不相同');
      return;
    }

    var datas = {
        username: _username,
        password: _password,
        flag:1
    };
    $.ajax({
        url: 'login.php',
        type: 'post',
        data: datas,
        dataType: 'text',
        success: function (result) {
            console.log(result);
            if (result === 'success') {
                alert('注册成功');
                window.location.reload();
            }else{
                alert("注册失败: "+result);
            }
        },
        error: function (date) {
            alert("注册失败"+date);
        }
  })
}

    var canSubmit = false;
    <?php 

        if(!isset($_COOKIE['uid']))
                $uid= 0;
        else{ 
            $uid  = $_COOKIE['uid'];
        }

    $rrr=$d->executeSql("select * from levels where vid=" .$vid. " and uid=". $uid);
    $rq= mysqli_fetch_assoc($rrr);
    if($rq){
        $stars = $rq['score'];
        echo "$('#rating').star('unbindEvent');";
        echo "canSubmit=true";
    }else{
        $stars = 0;
    }

    ?>


  //todo:  修改id
  function modify_f() {
            
            let username = $('#busername').val();
            if (username === '') {
                alert("必须输入用户名");
                return;
            }

            var form = new FormData();
            let password = $('#bpassword').val();
            if (password != '') {
                form.append("password", password);
            }

            let type = $("input[name = 'gender']:checked").val();
            console.log(type);
            let birthday = $('#birthday').val();
            let email = $('#email').val();
            var img_file = document.getElementById("xdaTanFileImg");
            var fileObj = img_file.files[0];

            if (typeof(fileObj) == "undefined") {
                console.log("img undefind");
            } else {
                form.append("img", fileObj);
            }

            form.append("username", username);
            form.append("type", type);
            form.append("birthday", birthday);
            form.append("email", email);
            form.append("user_pro", 1);

            console.log("编辑");
            var uid = $('#uid').val();
            form.append('uid', uid);
          
            $.ajax({
                url: 'admin/process.php',
                type: 'post',
                data: form,
                dataType: 'text',
                async: false,
                processData: false,
                contentType: false,
                success: function (result) {
                    console.log(result);
                    alert("修改信息成功");
                    window.location.reload();
                
                },
                error: function () {
                    alert("失败");
                    window.location.reload();
                }
            })
        }

    function logout(){
        setCookie("username", "", -1);  
        alert("登出");
        window.location.reload();
    }
    //设置cookie
    function setCookie(cname, cvalue, exdays) {
        var d = new Date();
        d.setTime(d.getTime() + (exdays*24*60*60*1000));
        var expires = "expires="+d.toUTCString();
        document.cookie = cname + "=" + cvalue + "; " + expires;
    }

    function modify(){
      $("#modify_label").text("修改个人信息");
      $('#modify_dialog').modal();
    }
    function getCookie(name)
    {
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
    return unescape(arr[2]);
    else
    return null;
    }

    $('#rating').star({
        modus: 'entire', //点亮模式 （‘half’半颗， ‘entire’整颗）
        total: 5, //默认共几颗星
        num: <?php echo $stars?>, //默认点亮个数
        readOnly: false, //默认是否只读，
        chosen: function(_count, total) { //点击后事件
        
            if(<?php 
                    if(!isset($_COOKIE['uid']))
                        echo 'true';
                    else{ 
                        echo 'false';
                    }
                    ?>){
                alert("登陆后才能评价");
                return;
            }
            count = _count;
            console.log("star: " + count);
            
            canSubmit= true;
        }     
    })
    var count;

    function submitComments(){
        if(canSubmit){

            let cmt = $("#cmt").val();

            let datas = {
            c:count,
            comment:cmt,
            vid:<?php echo  $vid?>,
            uid:<?php 
                if(!isset($_COOKIE['uid']))
                    echo 0;
                else{ 
                    echo $_COOKIE['uid'];
                }
                ?>
            }

            $.ajax({
                url: 'admin/process.php',
                type: 'post',
                data: datas,
                dataType: 'text',
                success: function (result) {
                    console.log(result);
                    if (result === 'success') {
                        alert('评价成功');
                        $('#rating').star('unbindEvent');
                        window.location.reload();
                    }else{
                        alert("评价失败1: "+ result);
                    }
                    
                },
                error: function (date) {
                    alert("评价失败2: "+date);
                }
            }
            )
        }else{
            alert("请先评分！");
        }
    }


</script>
<style>
*{
    margin:0;
    padding: 0;
}
body{
  background:#ddd;
}
h2{text-align: center;}
  .rating{
    position:relative;
    width:240px;
    height: 45px;
    background:url(assets/images/icon.png) repeat-x;
    margin:20px;
  } 
 
  .rating-disply{
    width:48px;
    height:45px;
    background-position:0 -45px;
    background:url(assets/images/icon.png) repeat-x 0 -44px;

  }
  .rating-mask{
    position:absolute;
    left:0;
    top:0;
    width:100%;
  }
  .rating-item{
    list-style: none;
    float: left;
    width:48px;
    height:45px;
    cursor:pointer;
  }
  
</style>
<script type="text/javascript">
    //判断浏览器是否支持FileReader接口
    if (typeof FileReader == 'undefined') {
        document.getElementById("xmTanDiv").InnerHTML = "<h1>当前浏览器不支持FileReader接口</h1>";
        //使选择控件不可操作
        document.getElementById("xmTanImg").setAttribute("disabled", "disabled");
    }

    //选择图片，马上预览
    function xmTanUploadImg(obj) {
        var file = obj.files[0];

        console.log(obj);
        console.log(file);
        console.log("file.size = " + file.size);  //file.size 单位为byte

        var reader = new FileReader();

        //读取文件过程方法
        reader.onloadstart = function (e) {
            console.log("开始读取....");
        }
        reader.onprogress = function (e) {
            console.log("正在读取中....");
        }
        reader.onabort = function (e) {
            console.log("中断读取....");
        }
        reader.onerror = function (e) {
            console.log("读取异常....");
        }
        reader.onload = function (e) {
            console.log("成功读取....");

            var img = document.getElementById("xmTanImg");
            img.src = e.target.result;
            //或者 img.src = this.result;  //e.target == this
        }

        reader.readAsDataURL(file)
    }
</script>
</html>