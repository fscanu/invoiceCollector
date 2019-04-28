var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
import app, { Component } from 'apprun';
var homeComponent = /** @class */ (function (_super) {
    __extends(homeComponent, _super);
    function homeComponent() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.state = 'home';
        _this.view = function (state) {
            return app.createElement("main", { role: "main", className: "container" },
                app.createElement("div", { className: "jumbotron" },
                    app.createElement("h1", null, "Navbar example"),
                    app.createElement("p", { className: "lead" }, "This example is a quick exercise to illustrate how the top-aligned navbar works. As you scroll, this navbar remains in its original position and moves with the rest of the page."),
                    app.createElement("a", { className: "btn btn-lg btn-primary", href: "../../components/navbar/", role: "button" }, "View navbar docs \u00BB")));
        };
        _this.update = {
            '#home': function (state) { return state; },
        };
        return _this;
    }
    return homeComponent;
}(Component));
export default homeComponent;
//# sourceMappingURL=home.js.map