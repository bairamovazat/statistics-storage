<#ftl encoding='UTF-8'>

<!DOCTYPE html>
<html>
     <#include "head.ftl">
<body>

<#include "header.ftl">

<div class="container-fluid">
    <div class="row">
        <div class="col-md-1 col-lg-2 col-xs-3">

        </div>
        <div class="col-12 col-sm-12 col-md-10 col-lg-8 col-xs-6">
            <form action="sign-up" method='POST'>
                <div class="form-group">
                    <#if error??>
                        <div class="alert alert-danger" role="alert">${error}</div>
                    </#if>
                </div>
                <div class="form-group">
                    <label for="inputName">Введите Ваше имя</label>
                    <input name="name" class="form-control" id="inputName" placeholder="Имя"/>
                </div>

                <div class="form-group">
                    <label for="inputLogin">Введите логин</label>
                    <input name="login" class="form-control" id="inputLogin" placeholder="Логин"/>
                </div>

                <div class="form-group">
                    <label for="inputPassword">Введите пароль</label>
                    <input name="password" type="password" class="form-control" id="inputPassword" placeholder="Пароль"/>
                </div>

                <div class="form-group">
                    <label for="inputEmail">Введите Ваш электронный адрес</label>
                    <input name="email" class="form-control" id="inputEmail" placeholder="Почта"/>
                </div>

                <div class="form-group">
                    <input type="submit" value="Зарегистрироваться" class="form-control" id="inputSubmit"/>
                </div>
            </form>
        </div>
        <div class="col-md-1 col-lg-2 col-xs-3">

        </div>
    </div>
</div>

<#include "footer.ftl">
</body>
</html>
