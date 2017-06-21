<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>FileServer</title>
<script src="js/jquery-3.2.1.min.js?ver=20170615"></script>
</head>
<body>
	<h1>FileServer TEST</h1>
	<h5>${rq.name?if_exists} / ${rq.age?if_exists} /
		${rq.job?if_exists}</h5>
	<h5>aws fileserver</h5>
	<table class="fileTable">
		<tr>
			<th>번호</th>
			<th>파일명</th>
			<th>크기</th>
		</tr>
		<#list list as item> <#if item.size?number==0>
		<#else>
		<tr>
			<td>${item_index+1}</td>
			<td><a href="${rq.awsLink}${item.realName?if_exists}" download >${item.oriName}</a></td>
			<td>${item.size} bytes</td>
		</tr>
		</#if> </#list>

	</table>

	<div style ="border: 1px dashed black; padding:20px;">
	<form id="form" enctype="multipart/form-data" style ="margin-bottom : 20px;">
		<input name="files" type="file" multiple />
	</form>
	
	<button id="submit">서브밋</button>
	</div>
	<script>
		$(document).ready(function() {

			$("#submit").click(function(e) {
				e.preventDefault();
				var form = $("#form")[0];
				var formData = new FormData(form);
				$.ajax({
					url : '${rq.path?if_exists}/file/upload',
					data : formData,
					processData : false,
					contentType : false,
					type : "POST",
					success : function(data) {
						console.log(data);
						location.reload();
					}
				})
			})
		});
	</script>
</body>
</html>