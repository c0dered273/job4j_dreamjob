<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.store.CandidatesPsqlStore" %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <title>Работа мечты</title>
</head>
<body>
<%
    String id = request.getParameter("id");
    Candidate candidate = new Candidate(0, "");
    if (id != null) {
        candidate = CandidatesPsqlStore.instOf().findById(Integer.parseInt(id));
    }
%>
<div class="container pt-3">
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новый кандидат.
                <% } else { %>
                Редактирование кандидата.
                <% } %>
            </div>
            <div class="card-body">
                <form id="newCandidate" action="<%=request.getContextPath()%>/candidates.do?id=<%=candidate.getId()%>" onsubmit="return validate()" method="post">
                    <div class="form-group">
                        <label for="candidateName">Имя</label>
                        <input type="text" class="form-control" id="candidateName" name="name" value="<%=candidate.getName()%>">
                    </div>
                    <div class="form-group">
                        <label for="citySel1">Город</label>
                        <select class="form-control" name="cityId" id="citySel1"></select>
                    </div>
                    <button type="submit" class="btn btn-primary m-1">Сохранить</button>
                    <a href="<c:url value="/candidates.do"/>" class="btn btn-danger m-1" role="button">Отмена</a>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(getCitiesJSON());
    function validate() {
        let result = true;
        $('#newCandidate :input').each(function () {
            if ($(this).attr('type') === 'text' && $(this).val() === '') {
                alert("Please fill " + $('label[for="' + this.id + '"]').html());
                result = false;
            }
        })
        return result;
    }
    function getCitiesJSON() {
        let url = 'http://localhost:8080/dreamjob/cities.do?action=getList';
        $.getJSON(url, function (data) {
            citiesDropDown(data);
        });
        return true;
    }
    function citiesDropDown(data) {
        let selectList = $("#citySel1");
        selectList.empty();
        selectList.append('<option selected disabled>Выберите город</option>');
        $.each(data.cities, function (key, entry) {
            selectList.append($('<option></option>').attr('value', key).text(entry));
        })
        return true;
    }
</script>
</body>
</html>