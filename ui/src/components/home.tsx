import app, { Component } from 'apprun';

export default class homeComponent extends Component {
    state = 'home';

    view = (state) => {
        return <main role="main" className="container">
            <div className="jumbotron">
                <h1>Navbar example</h1>
                <p className="lead">This example is a quick exercise to illustrate how the top-aligned navbar works.
                    As you scroll, this navbar remains in its original position and moves with the rest of the
                    page.</p>
                <a className="btn btn-lg btn-primary" href="../../components/navbar/" role="button">View navbar
                    docs &raquo;</a>
            </div>
        </main>
    };

    update = {
        '#home': state => state,
    }
}

