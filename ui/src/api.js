import app from 'apprun';
import {get, getToken, post, put, serializeObject, toQueryString} from './fetch';
// Conduit API
window['defaultBasePath'] = 'http://localhost:9090/api';
export { toQueryString, serializeObject };
export var auth = {
    current: function () { return getToken()
        ? get('/user')
        : null; },
    signIn: function (user) {
        return post('/users/login', { user: user });
    },
    register: function (user) {
        return post('/users', { user: user });
    },
    save: function (user) {
        return put('/user', { user: user });
    },
    authorized: function () { return app['user'] ? true : app.run('#/login') && false; } // app.run returns true if found event handlers
};
//# sourceMappingURL=api.js.map