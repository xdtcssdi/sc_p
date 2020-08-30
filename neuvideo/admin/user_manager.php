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

                    <li><a href="index.php"><i class="icon-home"></i> 管理面板</a></li>
                   
                    <li><a href="admin_manager.php"><i class="icon-bullhorn"></i> 管理员信息 </a></li>
                    <li><a href="video_manager.php"><i class="icon-bullhorn"></i> 视频管理信息 </a></li>
                    <li  class="active"><a href="user_manager.php"><i class="icon-bullhorn"></i> 用户管理信息 </a></li>
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
                    <li class="active">用户信息管理</li>
                </ul>

            </div>

            <div class="main-area dashboard">

                <div class="row">

                    <div class="span10">

                        <div class="slate">

                            <form class="form-inline" method="get">
                                <input type="text" class="input-large" placeholder="Keyword..." name="search">

                                <button type="submit" class="btn btn-primary">搜索</button>
                            </form>

                        </div>

                    </div>

                </div>

                <div class="row">

                    <div class="span10 listing-buttons">

                        <button class="btn btn-primary" onclick="openAddDialog();">Add New Item</button>

                    </div>

                    <div class="span10">

                        <div class="slate">

                            <table class="orders-table table">
                                <thead>
                                <tr>
                                    <th>用户名</th>
                                    <th>性别</th>
                                    <th>生日</th>
                                    <th>邮箱</th>
                                </tr>
                                </thead>
                                <tbody>
                                
                                
                                <?php
                                    include "requireDate.php";
                                    if (!isset($_GET['page'])) {
                                        $_GET['page'] = 1;
                                    }
                                    if (!isset($_GET['num'])) {
                                        $_GET['num'] = 10;
                                    }
                                    $kw = '';
                                    if (isset($_GET['search'])) {
                                        $kw = $_GET['search'];
                                    }
    
                                    $page = $_GET['page'];
                                    $num = $_GET['num'];
                                    $data = getUsersDate($page, $num, $kw);
    
                                    while ($row = mysqli_fetch_assoc($data[0])) {
                                        echo "<tr>
                                        <td><a href=\"#\" onclick='openEditDialog(\"" . $row['uid'] . "\",\"" . $row['uname'] . "\",\"" . $row['gender'] . "\",\"" . $row['birthdate'] . "\",\"" . $row['email'] . "\",\"" . $row['pic'] . "\");'>" . $row['uname'] . "</a><br/></td>
                                        <td>";
                                        echo $row['gender'] == 1 ? "女" : "男";
                                        echo "<td>" . $row['birthdate'] . "</td>";
                                        echo "<td>" . $row['email'] . "</td></tr>";
                                    }
                                ?>

                                </tbody>
                            </table>

                        </div>

                    </div>

                    <div class="span5">

                        <div class="pagination pull-left">
                            <ul>


                                <?php
                                $kw = '';
                                if (isset($_GET['search'])) {
                                    $kw = $_GET['search'];
                                }
                                $page = $_GET['page'];
                                $num = $_GET['num'];
                                $prev = $page == 1 ? 1 : $page - 1;
                                $next = $page == $data[1] ? $data[1] : $page + 1;
                                echo '<li><a href="user_manager.php?page=' . $prev . '&num=' . $num . '&search=' . $kw . '">Prev</a></li>';
                                for ($i = 1; $i <= $data[1]; ++$i) {
                                    echo '<li ';
                                    if ($i == $page) {
                                        echo 'class="active">';
                                    } else {
                                        echo '>';
                                    }
                                    echo '<a href="user_manager.php?page=' . $i . '&num=' . $num . '&search=' . $kw . '">' . $i . '</a>';
                                    echo '</li>';
                                }
                                echo '<li><a href="user_manager.php?page=' . $next . '&num=' . $num . '&search=' . $kw . '">Next</a></li>';

                                ?>


                            </ul>
                        </div>

                    </div>

                </div>

                <div class="row">

                    <div class="span10 footer">

                        <p>&copy; Website NeuVideo 2018</p>

                    </div>

                </div>

            </div>

        </div> <!-- end span10 -->

    </div> <!-- end row -->

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
                        <input hidden id="uid"/>

                    </div>
                    <div class="form-group">
                        <label for="txt_parentdepartment">密码</label>
                        <input type="password" name="password" class="form-control" id="password"
                               placeholder="密码">
                    </div>
                    <div class="form-group">
                        <label for="gender">性别</label>
                        <input type="radio" name="gender" value="0"  >男
                            &nbsp;
                        <input type="radio" name="gender" value="1" >女

                    </div>
                    <div class="form-group">
                        <label for="birthday">生日</label>
                        <input type="date" name="birthday" class="form-control" id="birthday">
                    </div>

                    <div class="form-group">
                        <label for="pic">头像</label>
                        <input type="file" id="xdaTanFileImg" onchange="xmTanUploadImg(this)" accept="image/*"/>
                        <img id="xmTanImg"/>
                        <div id="xmTanDiv"></div>
                    </div>
                    <div class="form-group">
                        <label for="email">邮箱</label>
                        <input type="email" name="email" class="form-control" id="email"
                               placeholder="邮箱">
                    </div>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" id="delMember">
                        删除
                    </button>
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
</div>
</div> <!-- end container -->

<!-- Le javascript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="../assets/js/jquery.min.js"></script>
<script src="../assets/js/bootstrap.js"></script>
<script src="../assets/js/excanvas.min.js"></script>
<script src="../assets/js/jquery.flot.min.js"></script>
<script src="../assets/js/jquery.flot.resize.js"></script>

<script type="text/javascript">

    Date.prototype.format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1,                 //月份
            "d+": this.getDate(),                    //日
            "h+": this.getHours(),                   //小时
            "m+": this.getMinutes(),                 //分
            "s+": this.getSeconds(),                 //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    };

    openEditDialog = function (uid, uname, gender, birthdate, email, pic) {
        $("#myModalLabel").text("编辑");
        document.getElementById('username').value = uname;
        document.getElementById('password').value = '';
        document.getElementById('birthday').value = birthdate;
        document.getElementById('uid').value = uid;
        document.getElementById('email').value = email;
        document.getElementById('xmTanImg').src = pic;
if(gender==0){
$("input[name='gender']").eq(0).attr("checked","checked");
$("input[name='gender']").eq(1).removeAttr("checked");
$("input[name='gender']").eq(0).click();
}else{
$("input[name='gender']").eq(1).attr("checked","checked");
$("input[name='gender']").eq(0).removeAttr("checked");
$("input[name='gender']").eq(1).click();
}
        $("#delMember").show();
        $('#myModal').modal();
    };
    openAddDialog = function () {
        $("#myModalLabel").text("新增");
        $("#delMember").hide();
        document.getElementById('username').value = '';
        document.getElementById('password').value = '';
        document.getElementById('birthday').value = '';
        document.getElementById('uid').value = '';
        document.getElementById('email').value = '';
        document.getElementById('xmTanImg').src = '';
        $('#myModal').modal();
    };

    $(function () {
        $("#btn_submit").click(function () {
            let username = $('#username').val();
            if (username === '') {
                alert("必须输入用户名");
                return;
            }

            var form = new FormData();

            let password = $('#password').val();
            if (password != '') {

                form.append("password", password);
            }

            let type = $("input[name = 'gender']:checked").val();
            
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

            if ($("#myModalLabel").text() == "新增") {
                console.log('新增');
                if (password === '') {
                    alert("必须输入密码");
                    return;
                }
            } else {
                console.log("编辑");
                var uid = $('#uid').val();
                form.append('uid', uid);
            }

            $.ajax({
                url: 'process.php',
                type: 'post',
                data: form,
                dataType: 'text',
                async: false,
                processData: false,
                contentType: false,
                success: function (result) {
                    console.log(result);
                    if (result -1==0) {
                        alert('添加成功');
                        window.location.reload();
                    } else{
                        alert("存在该用户，修改信息编辑成功");
                        window.location.reload();
                    }
                },
                error: function () {
                    alert("失败");
                    window.location.reload();
                }
            })
        })
    });

    $(function () {
        $("#delMember").click(function () {
            let name = document.getElementById('username').value;
            var form = new FormData();
            form.append("username", name);
            var datas = {
                username: name,
                del: 2
            };
            $.ajax({
                url: 'process.php',
                type: 'post',
                data: datas,
                dataType: 'text',
                success: function (result) {
                    console.log(result);
                    if (result -3==0) {
                        alert('删除成功');
                        window.location.reload();
                    }else{
                        alert('删除失败');
                    }
                },
                error: function () {
                    alert("删除失败");
                    window.location.reload();
                }
            })
        })
    });
</script>
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