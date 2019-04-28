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
var signinComponent = /** @class */ (function (_super) {
    __extends(signinComponent, _super);
    function signinComponent() {
        var _this = _super !== null && _super.apply(this, arguments) || this;
        _this.state = 'signin';
        _this.view = function (state) {
            return app.createElement("div", { class: "container" },
                app.createElement("h1", null, state));
        };
        _this.update = {
            '#signin': function (state) { return state; },
        };
        return _this;
    }
    return signinComponent;
}(Component));
export default signinComponent;
//# sourceMappingURL=signin.js.map