<?php

$username=$_POST['username'];
$password=$_POST['password'];


include '../database.php';
$p = new DataBase();

$sql = "select * from admins where adminname='".$username."'" . "  and password = '".$password ."'";
//echo $sql;
$res = $p->executeSql($sql);
$n = mysqli_num_rows($res);
if($n>0){
    setcookie('adminusername',$username,time()+3600*24);
    setcookie('adminpassword',$password,time()+3600*24);
    header("location:index.php");
}else{
   echo "<script>alert('用户名密码错误，请重试')</script>";
   header("location:login.php");
}

?>