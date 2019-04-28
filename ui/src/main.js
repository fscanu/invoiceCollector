import app from 'apprun';
import home from './components/home';
import signin from './components/signin';
import signup from './components/signup';
import header from './components/header';
//mounting the header in the div inside the html
var index_header = document.getElementById('header');
new header().mount(index_header);
var element = document.getElementById('my-app');
new home().mount(element);
new signin().mount(element);
new signup().mount(element);
app.on('//', function (route) {
    var menus = document.querySelectorAll('.nav li');
    for (var i = 0; i < menus.length; ++i) {
        menus[i].classList.remove('active');
    }
    var menu = document.querySelector("[href='" + route + "']");
    menu && menu.parentElement.classList.add('active');
});
app.on('debug', function (p) { return console.log(p); });
//# sourceMappingURL=main.js.map