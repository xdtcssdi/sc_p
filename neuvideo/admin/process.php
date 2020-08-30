<?php

include '../database.php';
$p = new DataBase();


date_default_timezone_set("Asia/Shanghai");

if(isset($_POST['c'])){

    $sql = "select * from comments where uid=".$_POST['uid']." and vid=".$_POST['vid'];
    //echo $sql;
    $res = $p->executeSql($sql);
    $r = mysqli_num_rows($res);
    if($r>0){
    	echo "只能评价一次";
	    return;
    }
	
    $res1 = $p->insert("levels",array('vid','uid','score'),array($_POST['vid'],$_POST['uid'],$_POST['c']));
    $res2 = $p->insert("comments",array('content','cdate','uid','vid'),array($_POST['comment'],date("y-m-d H:i:s", time()),$_POST['uid'],$_POST['vid']));

    if($res1&&$res2){
        echo "success";
    }else{
        echo "fail";
    }
    return;
}


$username = '';

if (isset($_POST['username'])) {
    $username = $_POST['username'];
}

//todo： 删除图片
if (isset($_POST['del'])) {
    $del = $_POST['del'];

    if ($del == 3) {
        $p->executeSql("DELETE FROM videos WHERE vid=" . $_POST['vid']);
    } else if ($del == 1){
        $p->executeSql("DELETE FROM admins WHERE adminname='" . $username . "'");
    }else if ($del == 2){
        $p->executeSql("DELETE FROM users WHERE uname='" . $username . "'");
    }else if ($del == 4){
        $p->executeSql("DELETE FROM comments WHERE cid='" . $_POST['cid'] . "'");
    }else if($del ==5){
        $p->executeSql("DELETE FROM videotype WHERE tid='" . $_POST['tid'] . "'");
    }
    echo '3';
    return;
}

if (isset($_POST['user_pro'])){

    $uname = $_POST['username'];
    $gender = $_POST['type'];
    $birthday = $_POST['birthday'];
    $email = $_POST['email'];
    $img = '';
    $filename = '';
    $t='';
    if (isset($_FILES['img'])) {
        $img = $_FILES['img'];
        $t = time() . $img["name"];
        $filename = "./img/" . $t;
        $filename = iconv("UTF-8", "gb2312", $filename);
        move_uploaded_file($img["tmp_name"], $filename);
    }
    $password ='';
    if (isset($_POST['password'])){
        $password =$_POST['password'];
    }

    if (isset($_POST['uid'])){
        //edit
        $uid =$_POST['uid'];
        if (isset($_POST['password'])&&isset($_FILES['img'])){
            $sql1="update users set uname = '$uname', gender='$gender',birthdate='$birthday',pic='img/$t',email='$email', password='$password' where uid = $uid";
            $p->executeSql($sql1);
            echo "2";
            return;
        }else if (isset($_POST['password'])&&!isset($_FILES['img'])){

            $sql1="update users set uname = '$uname', gender='$gender',birthdate='$birthday',email='$email', password='$password' where uid = $uid";
            $p->executeSql($sql1);
            echo "3";
            return;
        }else if (!isset($_POST['password'])&&isset($_FILES['img'])){
            $sql1="update users set uname = '$uname', gender='$gender',birthdate='$birthday',pic='img/$t',email='$email' where uid = $uid";
            echo "4";
            $p->executeSql($sql1);
            return;
        }else{
            $sql1="update users set uname = '$uname', gender='$gender',birthdate='$birthday',email='$email' where uid = $uid";
            //echo $sql1;
            $p->executeSql($sql1);
            echo "5";
            return;
        }

    }else{
        //insert
    $p->insert("users",array("uname","password","gender","birthdate","pic","email"),array($uname,$password,$gender,$birthday,'img/'.$t,$email));
        echo "1";
        return;
    }
}


if (isset($_POST['videoname'])) {
    
    $address = $_POST['address'];
    $videoname = $_POST['videoname'];
    $type = $_POST['type'];
    
    $img = '';
    $filename = '';
    $t='';
    if (isset($_FILES['img'])) {
        $img = $_FILES['img'];
        $t = time() . $img["name"];
        $filename = "./img/" . $t;
        $filename = iconv("UTF-8", "gb2312", $filename);
        move_uploaded_file($img["tmp_name"], $filename);
    }

    $uploadadmin = $_POST['uploadadmin'];

    $f = mysqli_fetch_assoc($p->executeSql("select * from admins where adminname=\"" . $uploadadmin . "\""));
    if($f){
        $uploadadmin = $f['adminid'];
    }else{
        $uploadadmin = '0';
    }
    $intro = $_POST['intro'];
    
    if (isset($_POST['vid'])) {
        $upsql='';
        $time = date("y-m-d H:i:s",time());
        if ($t==''){
            $p->update("videos",array("videoname","intro","uploaddate","uploadadmin","tid","address"),array($videoname,$intro,$time,$uploadadmin,$type,$address),"vid",$_POST['vid']);
        }
        else{
            $p->update("videos",array("videoname","intro","uploaddate","uploadadmin","tid","address","pic"),array($videoname,$intro,$time,$uploadadmin,$type,$address,'img/'.$t),"vid",$_POST['vid']);
        }  

        echo '0';
        return;
    }

    $p->insert('videos', array('videoname', 'tid', 'pic', 'intro', 'uploaddate', 'uploadadmin', 'address'),
        array($videoname, $type, 'img/'.$t, $intro, date("y-m-d H:i:s", time()), $uploadadmin, $address)
    );
    echo '1';
    return;
}

if (isset($_POST['admin'])) {
$pwd = $_POST['password'];

$res = $p->selectALL('admins');
if($res){
while ($r = mysqli_fetch_assoc($res)) {
    if (isset($r['adminname'])) {
        if ($r['adminname'] == $username) {
            $p->executeSql('update admins set password="' . $pwd .
                '" where adminid="' . $r['adminid'] . '"');
            echo '2';
            return;
        }
    }
}
}

$p->insert('admins', array('adminname', 'password'), array($username, $pwd));
echo '1';
}

if(isset($_POST['cid'])){
    if(isset($_POST['comment']))
    { 
        $p->executeSql('update comments set content= "' . $_POST['comment'] . '" , cdate="'.date("y-m-d H:i:s", time()).
        '" where cid="' . $_POST['cid'] . '"');
        echo "1";
        return;
    }
}


 
if(isset($_POST['add_'])){

    $p->executeSql('update videotype set typename= "' . $_POST['typename'].
    '" where tid="' . $_POST['tid'] . '"');
    echo "1";
    return;
}

if (isset($_POST['typename'])) {
    $sql = "select * from videotype where typename='". $_POST['typename'] ."'";
    //echo $sql;
    $res = $p->executeSql($sql);
    $r = mysqli_num_rows($res);
    if($r>0){
        echo "2";
    }else{
        $p->insert('videotype', array('typename'), array($_POST['typename']));
        echo '1';
    }
    return;
}



?>