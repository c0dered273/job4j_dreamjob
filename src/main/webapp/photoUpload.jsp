<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Upload</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container">
    <a href="<c:url value="/candidates.do"/>" class="btn btn-primary btn-sm m-1" role="button">Назад</a>
    <table class="table">
        <thead>
        <tr>
            <th>URL</th>
            <th>View</th>
        </tr>
        </thead>
        <tbody>
            <tr valign="top">
                <td><a href="<c:url value='/download?id=${id}'/>">${id}.jpg</a></td>
                <td>
                    <img src="<c:url value='/download?id=${id}'/>" width="100px" height="100px"/>
                </td>
            </tr>
        </tbody>
    </table>
    <h2>Upload image</h2>
    <form action="<c:url value='/upload'/>" method="post" enctype="multipart/form-data">
        <div class="checkbox">
            <input type="hidden" name="id" value="${id}">
            <input type="file" name="file">
        </div>
        <button type="submit" class="btn btn-primary m-1">Submit</button>
    </form>
</div>

</body>
</html>