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
                    <li><a href="user_manager.php"><i class="icon-bullhorn"></i> 用户管理信息 </a></li>
                    <li class="active" ><a href="comment_manager.php"><i class="icon-bullhorn"></i> 评论信息管理 </a></li>
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
                    <li class="active">评论信息管理</li>
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

                    <div class="span10">

                        <div class="slate">

                            <table class="orders-table table">
                                <thead>
                                <tr>
                                    <th>用户名</th>
                                    <th>视频名</th>
                                    <th>评论</th>
                                    <th class="actions">时间</th>
                                </tr>
                                </thead>
                                <tbody>
                                <?php
                                include "requireDate.php";
                                
                                if (!isset($_GET['page'])){
                                    $_GET['page']=1;
                                }
                                if (!isset($_GET['num'])){
                                    $_GET['num']=10;
                                }
                                $kw='';
                                if (isset($_GET['search'])){
                                    $kw=$_GET['search'];
                                }

                                $page = $_GET['page'];
                                $num = $_GET['num'];
                                
                                $data = getCommentsDate($page, $num, $kw);
                                            
                                while ($row = mysqli_fetch_assoc($data[0])) {
                                    echo "<tr>
                                    <td><a href=\"#\" onclick='openEditDialog(\"" . $row['content'] . "\",\"" . $row['cid'] . "\");'>" . $row['uname'] . "</a><br/></td>
                                    <td>" . $row['videoname'] . "</td>"."<td>" . $row['content'] . "</td>". "<td>" . $row['cdate'] . "</td>". "</tr>";
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
                                $kw='';
                                if (isset($_GET['search'])){
                                    $kw=$_GET['search'];
                                }
                                $page = $_GET['page'];
                                $num = $_GET['num'];
                                $data = getCommentsDate($page, $num,$kw);
                                $prev = $page == 1 ? 1 : $page - 1;
                                $next = $page == $data[1] ? $data[1] : $page + 1;
                                echo '<li><a href="comment_manager.php?page=' . $prev . '&num=' . $num .'&search='.$kw.'">Prev</a></li>';
                                for ($i = 1; $i <= $data[1]; ++$i) {
                                    echo '<li ';
                                    if ($i == $page) {
                                        echo 'class="active">';
                                    } else {
                                        echo '>';
                                    }
                                    echo '<a href="comment_manager.php?page=' . $i . '&num=' . $num .'&search='.$kw. '">' . $i . '</a>';
                                    echo '</li>';
                                }
                                echo '<li><a href="comment_manager.php?page=' . $next . '&num=' . $num .'&search='.$kw. '">Next</a></li>';

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
                    
                        <label for="comment">评论</label>
                        <input type='text' id='cid'/>
                        <input type="text" name="comment" class="form-control" id="comment"
                               placeholder="评论">
                              
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
    openEditDialog = function (comment,cid) {
        $("#myModalLabel").text("编辑");
        $("#cid").hide();
        $("#comment").val(comment);
        
        $("#cid").val(cid);
        $("#delMember").show();
        $('#myModal').modal();
    };

    $(function(){
        $("#btn_submit").click(function() {

            let comment=$("#comment").val();
            let cid =  $("#cid").val();
            var datas={
                cid:cid,
                comment:comment
            };
            $.ajax({
                url:'process.php',
                type:'post',
                data:datas,
                dataType:'text',
                success:function(result){
                    console.log(result)
                    if(result-1==0){
                        alert('编辑成功');
                        window.location.reload();
                    }else {
                        alert("编辑失败");
                        window.location.reload();
                    }
                },
                error:function(data){
                    console.log(data);
                    alert("失败");
                    window.location.reload();
                }
            })
        })
    });

    $(function(){
        $("#delMember").click(function() {
            let cid = $("#cid").val();
            console.log(cid);
            var datas={
                cid:cid,
                del:4
            };
            $.ajax({
                url:'process.php',
                type:'post',
                data:datas,
                dataType:'text',
                success:function(result){
                    if(result-3==0){
                        alert('删除成功');
                        window.location.reload();
                    }else{
                        alert('删除失败');
                        window.location.reload();
                    }
                },
                error:function(){
                    alert("删除失败");
                    window.location.reload();
                }
            })
        })
    });
    function logout(){
    
    setcookie('adminusername','',time()-1);
    setcookie('adminpassword','',time()-1);
    header("location:login.php");
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