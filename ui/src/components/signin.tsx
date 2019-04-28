import app, { Component } from 'apprun';

export default class signinComponent extends Component {
    state = 'signin';

    view = (state) => {
        return <div class="container">
            <h1>{state}</h1>
        </div>
    };

    update = {
        '#signin': state => state,
    }
}

