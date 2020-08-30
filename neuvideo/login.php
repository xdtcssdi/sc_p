<?php
/**
 * Created by PhpStorm.
 * User: computer
 * Date: 2018/10/24
 * Time: 17:13
 */
//获取登录数据
$username=$_POST['username'];
$password=$_POST['password'];
$flag = $_POST['flag'];

// if(isset($_GET["reme"])){
//     setcookie('reme',true,time()+3600*24);
//     setcookie('username',$username,time()+3600*24);
//     setcookie('password',$password,time()+3600*24);
// }else{
//     setcookie('reme',false,time()+3600*24);
//     setcookie('username','',time()+3600*24);
//     setcookie('password','',time()+3600*24);
// }

//验证密码
include "./database.php";

$d = new DataBase();

if($flag=="0"){

    $res=$d->executeSql('select * from users where uname="'.$username."\"");
    $num = mysqli_num_rows($res);
    if($num>0){
        $row = mysqli_fetch_assoc($res);

        if ($row['password'] != $password){
            echo "密码错误";
            //setcookie('error','密码错误');
            //echo '<script>alert("密码错误")</script>';
            //header('location: index.php');
        }else{
            setcookie('username',$username,time()+3600*24);
            setcookie('uid',$row['uid'],time()+3600*24);
            echo "success";
        }
    }else{
        echo "该用户不存在";
    }
}else if($flag=="1"){
    $res=$d->executeSql('select * from users where uname="'.$username."\"");
    
    if(mysqli_num_rows($res)){
        echo "用户已存在";
    }else{

        $res = $d->insert("users",array("uname","password","gender","birthdate","email"),array($username,$password,0,"1970-01-01",""));
        if($res==1){
            echo "success";
        }else{
            echo "创建失败";
        }
    }
}