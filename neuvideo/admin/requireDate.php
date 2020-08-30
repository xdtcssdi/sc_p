<?php
/**
 * Created by PhpStorm.
 * User: computer
 * Date: 2018/10/24
 * Time: 19:23
 */
include ('../database.php');

function getAdminDate($pageNum, $pageSize, $kw){

    $p=new DataBase();
    $rs = "select * from admins limit " . (($pageNum - 1) * $pageSize) . "," . $pageSize;
    $rs1='select count(*) as number from admins';
    if ($kw!=''){
        $rs= 'select * from admins where adminname like "%'.  $kw  .'%" limit ' . (($pageNum - 1) * $pageSize) . "," . $pageSize;
        $rs1='select count(*) as number from admins where adminname like "%'.$kw.'%"';
    }

    $r = $p->executeSql($rs);
    $c = $p->executeSql($rs1);

    $res = mysqli_fetch_assoc($c);

    return array($r,intval($res['number']/$pageSize)+1);
}

function getVideoDate($pageNum, $pageSize, $kw){

    $p=new DataBase();
    $rs = "select * from videos limit " . (($pageNum - 1) * $pageSize) . "," . $pageSize;
    $rs1='select count(*) as number from videos';
    if ($kw!=''){
        $rs= 'select * from videos where videoname like "%'.  $kw  .'%" limit ' . (($pageNum - 1) * $pageSize) . "," . $pageSize;
        $rs1='select count(*) as number from videos where videoname like "%'.$kw.'%"';
    }

    $r = $p->executeSql($rs);
    $c = $p->executeSql($rs1);

    $res = mysqli_fetch_assoc($c);

    return array($r,intval($res['number']/$pageSize)+1);
}


function getUsersDate($pageNum, $pageSize, $kw){

    $p=new DataBase();
    $rs = "select * from users limit " . (($pageNum - 1) * $pageSize) . "," . $pageSize;
    $rs1='select count(*) as number from users';
    if ($kw!=''){
        $rs= 'select * from users where uname like "%'.  $kw  .'%" limit ' . (($pageNum - 1) * $pageSize) . "," . $pageSize;
        $rs1='select count(*) as number from users where uname like "%'.$kw.'%"';
    }

    $r = $p->executeSql($rs);
    $c = $p->executeSql($rs1);

    $res = mysqli_fetch_assoc($c);

    return array($r,intval($res['number']/$pageSize)+1);
}

function getCommentsDate($pageNum, $pageSize, $kw){

    $p=new DataBase();
    $rs = "SELECT comments.cid as cid, comments.vid as vid,comments.uid as uid,comments.content as content ,videos.videoname as videoname,users.uname as uname ,comments.cdate as cdate FROM videos JOIN comments ON videos.vid = comments.vid  JOIN users ON comments.uid = users.uid limit " . (($pageNum - 1) * $pageSize) . "," . $pageSize;
    
    $rs1='select count(*) as number from comments';
    if ($kw!=''){
        $rs= 'SELECT comments.cid as cid,comments.vid as vid,comments.uid as uid,comments.content as content ,videos.videoname as videoname,users.uname as uname ,comments.cdate as cdate FROM videos JOIN comments ON videos.vid = comments.vid  JOIN users ON comments.uid = users.uid where content like "%'.  $kw  .'%" limit ' . (($pageNum - 1) * $pageSize) . "," . $pageSize;
        $rs1='SELECT count(*) as number FROM videos JOIN comments ON videos.vid = comments.vid  JOIN users ON comments.uid = users.uid where content like "%'.$kw.'%"';
    }

    $r = $p->executeSql($rs);
    $c = $p->executeSql($rs1);

    $res = mysqli_fetch_assoc($c);

    return array($r,intval($res['number']/$pageSize)+1);
}


function getVideoTypeDate($pageNum, $pageSize, $kw){

    $p=new DataBase();
    $rs = "select * from videotype limit " . (($pageNum - 1) * $pageSize) . "," . $pageSize;
    $rs1='select count(*) as number from videotype';
    if ($kw!=''){
        $rs= 'select * from videotype where typename like "%'.  $kw  .'%" limit ' . (($pageNum - 1) * $pageSize) . "," . $pageSize;
        $rs1='select count(*) as number from videotype where typename like "%'.$kw.'%"';
    }

    $r = $p->executeSql($rs);
    $c = $p->executeSql($rs1);

    $res = mysqli_fetch_assoc($c);

    return array($r,intval($res['number']/$pageSize)+1);
}