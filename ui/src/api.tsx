import app from 'apprun';
import {get, getToken, post, put, serializeObject, toQueryString} from './fetch';
import {IProfile, IUser} from './models';

// Conduit API
window['defaultBasePath'] = 'http://localhost:9090/api';


export { toQueryString, serializeObject }

export interface IAuthResponse {
    user: IUser
}

export interface IProfileResponse {
    profile: IProfile
}

export const auth = {
    current: () => getToken()
        ? get<IAuthResponse>('/user')
        : null,
    signIn: (user: { email: string, password: string }) =>
        post<IAuthResponse>('/users/login', { user }),
    register: (user: { username: string, email: string, password: string }) =>
        post<IAuthResponse>('/users', { user }),
    save: user =>
        put('/user', { user }),
    authorized: (): boolean => app['user'] ? true : app.run('#/login') && false // app.run returns true if found event handlers
};