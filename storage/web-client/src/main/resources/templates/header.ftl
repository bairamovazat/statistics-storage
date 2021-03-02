<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
    <a class="navbar-brand" href="#">Navbar</a>

    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsingNavbarLg">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="navbar-collapse collapse" id="collapsingNavbarLg">
        <ul class="navbar-nav">
            <#if model.user.isPresent()>
                <li class="nav-item">
                    <a class="nav-link" href="profile">Личный кабинет</a>
                </li>

                <li class="nav-item">
                    <a class="nav-link" href="logout">Выход</a>
                </li>
            </#if>

             <#if !model.user.isPresent()>
                <li class="nav-item">
                    <a class="nav-link" href="login">Авторизация</a>
                </li>
             </#if>
        </ul>
    </div>
</nav>