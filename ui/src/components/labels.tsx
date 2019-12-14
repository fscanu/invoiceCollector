import app, {Component, on} from 'apprun';

export default class labelsComponent extends Component {
    state = 'labels';

    view = (state) => {

        if (!state.labels) return;
        return <div class="w-75 p-3" className="table-responsive-sm">
            <table className="table table-hover">
                <thead className="thead-dark">
                <tr class="w-75 d-flex">
                    <th class="col-1">#</th>
                    <th class="col-3">Id</th>
                    <th class="col-3">Name</th>
                </tr>
                </thead>
                <tbody>
                {state.labels.map((label, idx) =>
                    <tr class="w-75 d-flex">
                        <th class="col-sm-1" scope="row">{idx+1}</th>
                        <td class="col-sm-3">{label.id}</td>
                        <td class="col-sm-3">{label.name}</td>
                    </tr>)}
                </tbody>
            </table>
        </div>
    }

    @on('/set-labels') setLabels = (state, labels) => ({...state, labels})
    update = {
        '#labels': state => state,
    }
}