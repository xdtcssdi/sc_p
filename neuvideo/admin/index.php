<?php
if(!isset($_COOKIE['adminusername']))
{
    echo "<script>alert('2秒后跳转')</script>";
    header("Refresh:2;url=login.php");
    return;
}
?>
<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="utf-8">
    <title>后台管理页面</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="http://fonts.googleapis.com/css?family=Oxygen|Marck+Script" rel="stylesheet" type="text/css">
    <link href="../assets/css/bootstrap.css" rel="stylesheet">
    <link href="../assets/css/font-awesome.css" rel="stylesheet">
    <link href="../assets/css/admin.css" rel="stylesheet">

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

</head>
<body>

<div class="container">

    <div class="row">

        <div class="span2">

            <div class="main-left-col">

                <h1><i class="icon-shopping-cart icon-large"></i> Adminize</h1>

                <ul class="side-nav">

                    <li class="active">
                        <a href="index.php"><i class="icon-home"></i> 管理面板</a>
                    </li>
                    <li><a href="admin_manager.php"><i class="icon-bullhorn"></i> 管理员信息 </a></li>
                    <li><a href="video_manager.php"><i class="icon-bullhorn"></i> 视频管理信息 </a></li>
                    <li><a href="user_manager.php"><i class="icon-bullhorn"></i> 用户管理信息 </a></li>
                    <li ><a href="comment_manager.php"><i class="icon-bullhorn"></i> 评论信息管理 </a></li>
                    <li ><a href="videotype_manager.php"><i class="icon-bullhorn"></i> 视频类型管理 </a></li>

                </ul>

            </div> <!-- end main-left-col -->

        </div> <!-- end span2 -->

        <div class="span10">

            <div class="secondary-masthead">

                <ul class="nav nav-pills pull-right">
                    <li>
                        <a href="../index.php"><i class="icon-globe"></i> 首页</a>
                    </li>
                    <li>
                    <a  href="#" onclick="logout()"><i class="icon-user"></i> <?php echo $_COOKIE['adminusername']?>
                        </a>

                    </li>
                </ul>

                <ul class="breadcrumb">
                    <li>
                        <a href="#">Admin</a> <span class="divider">/</span>
                    </li>
                    <li class="active">管理面板</li>
                </ul>

            </div>

            <div class="main-area dashboard">

                <div class="row">

                    <div class="span10">

                        <div class="slate clearfix">

                            <a class="stat-column" href="#">

                                <span class="number">
                                <?php
                               include '../database.php';
                               $p = new DataBase();
                               
                              $r =  mysqli_num_rows($p->executeSql("select * from videos"));
                               echo $r;
                                ?>
                                
                                
                                </span>
                                <span>视频数量</span>

                            </a>

                            <a class="stat-column" href="#">

                                <span class="number"><?php
                                $r =  mysqli_num_rows($p->executeSql("select * from users"));
                               echo $r;
                               ?>
                                </span>
                                <span>用户数量</span>

                            </a>

                            <a class="stat-column" href="#">

                                <span class="number"><?php
                                $r =  mysqli_num_rows($p->executeSql("select * from comments"));
                               echo $r;
                               ?></span>
                                <span>评论数量</span>

                            </a>

                            <a class="stat-column" href="#">

                                <span class="number"><?php
                                $r =  mysqli_num_rows($p->executeSql("select * from videotype"));
                               echo $r;
                               ?>
                               </span>
                                <span>分类数量</span>

                            </a>

                        </div>

                    </div>

        </div> <!-- end row -->

    </div> <!-- end container -->

    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="../assets/js/jquery.min.js"></script>
    <script src="../assets/js/bootstrap.js"></script>
    <script src="../assets/js/excanvas.min.js"></script>
    <script src="../assets/js/jquery.flot.min.js"></script>
    <script src="../assets/js/jquery.flot.resize.js"></script>

<script>
    
function setCookie(name,value) 
{ 
    var Days = 30; 
    var exp = new Date(); 
    exp.setTime(exp.getTime() + Days*24*60*60*1000); 
    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString(); 
} 

//读取cookies 
function getCookie(name) 
{ 
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
 
    if(arr=document.cookie.match(reg))
 
        return unescape(arr[2]); 
    else 
        return null; 
} 

function delCookie(name) 
{ 
    var exp = new Date(); 
    exp.setTime(exp.getTime() - 1); 
    var cval=getCookie(name); 
    if(cval!=null) 
        document.cookie= name + "="+cval+";expires="+exp.toGMTString(); 
} 


        function logout(){
    
    if (confirm("确认登出")==true){ 
        
    delCookie('adminusername');
    delCookie('adminpassword');
    self.location.href="login.php";
        header("location:login.php");
return true; 
}else{ 
return false; 
} 

}
    </script>

</body>
</html>