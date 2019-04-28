import app from 'apprun';

import home from './components/home';
import signin from './components/signin';
import signup from './components/signup';
import header from './components/header';
import linkgmail from './components/linkgmail';
//mounting the header in the div inside the html
const index_header = document.getElementById('header');
new header().mount(index_header);


const element = document.getElementById('my-app');
new home().mount(element);
new signin().mount(element);
new signup().mount(element);
new linkgmail().mount(element);

app.on('//', route => {
    const menus = document.querySelectorAll('.nav li');
    for (let i = 0; i < menus.length; ++i) {
        menus[i].classList.remove('active');
    }
    const menu = document.querySelector(`[href='${route}']`);
    menu && menu.parentElement.classList.add('active');
});

app.on('debug', p => console.log(p));