
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>NeuVideo</title>
<link href="./favicon.ico" rel="shortcut icon" />
<meta name="author" content="fly">

<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<script language="javascript" src="assets/js/jquery.min.js"></script>
<link href="assets/css/play.css" rel="stylesheet" type="text/css">

</head>
<body>


<div class="head-v3">


    <div class="centers">
          <select style="border:0px;font-size:18px" onchange="xuanzejiekou(this)">
          	<option value="http://jqaaa.com/jx.php?url=">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;选择接口</option>
            <option value="http://api.wlzhan.com/sudu/?url=">万能接口1</option>
			<option value="http://api.47ks.com/webcloud/?v=">万能接口2</option>	
            <option value="http://jiexi.071811.cc/jx2.php?url=">万能接口3</option>
            <option value="http://jqaaa.com/jq3/?url=&url=">万能接口4</option>
            <option value="http://yun.baiyug.cn/vip/index.php?url=">万能接口5</option>
            <option value="https://jiexi.071811.cc/jx2.php?url=">万能接口6</option>
			<option value="http://api.xiaomil.com/a/index.php?url=">腾讯视频接口1</option>	
			<option value="http://api.pucms.com/?url=">爱奇艺超清接口1</option>
            <option value="http://api.baiyug.cn/vip/index.php?url=">爱奇艺超清接口2</option>
            <option value="https://api.flvsp.com/?url=">爱奇艺超清接口3</option>
            <option value="http://api.xfsub.com/index.php?url=">芒果TV超清接口</option>
            <option value="http://65yw.2m.vc/chaojikan.php?url=">芒果TV手机接口</option>
            <option value="http://www.82190555.com/index/qqvod.php?url=">优酷超清接口</option>
			<option value="http://vip.jlsprh.com/index.php?url=">搜狐视频接口</option>
            <option value="http://2gty.com/apiurl/yun.php?url=">乐视视频接口</option>
          </select>
    <div class="searchs">
        <div class="from">

        <?php
        $vid = $_GET['vid'];
        include("database.php");

        $d= new DataBase();

        $r = $d->executeSql("select * from videos where vid=".$vid);
    
        $res = mysqli_fetch_assoc($r);
        
        ?>
          <input name="url" id="url" type="text" class="text" value="<?php echo $res['address']?>" >

          <input name="doplayers" type="button" id="doplayers" class="button" onclick="jiexi()" value="立即播放">
        </div>
        <div id="clear"></div>
    </div>
    <div id="ckplays">
    	<iframe id="jiekou" name="jiekou" src="./play.html" width="100%" height="650px"></iframe>
    </div> 

    
<script type="text/javascript">
players();
$("#doplayers").bind("click", function() {
    players()
});

function players() {
    var a = $('#url').val();
    if ($('#url').val() == "") {
        alert('请输入视频网站网址！');
        $('#url').focus();
        return (false)
    }
}
var jiekou = ""
function jiexi(){
	var url = document.getElementById("url").value;
	var dizhi=""
	
	if(url.indexOf("://")==-1){
		url = "http://"+document.getElementById("url").value;
	}
	
	if(jiekou==""){
		document.getElementById("jiekou").src = "https://jx.lache.me/cc/?url=" + url;
	}else{
		document.getElementById("jiekou").src = jiekou + url;
	}
}

function xuanzejiekou(v){
	 var url = document.getElementById("url").value;
	if(url==""){
		alert('请输入视频网站网址！');
	}else{
		jiekou = v.value
	}
}
</script>
</div>
        
</body></html>

</body></html>