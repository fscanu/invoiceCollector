var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var access_token = window && window.localStorage && window.localStorage.getItem('jwt') || '';
export function getToken() {
    return access_token;
}
export function setToken(token) {
    access_token = token;
    if (!window.localStorage)
        return;
    if (token)
        window.localStorage.setItem('jwt', token);
    else
        window.localStorage.removeItem('jwt');
}
export function fetchAsync(method, url, body) {
    return __awaiter(this, void 0, void 0, function () {
        var headers, response, result;
        return __generator(this, function (_a) {
            switch (_a.label) {
                case 0:
                    headers = { 'Content-Type': 'application/json; charset=utf-8' };
                    if (access_token)
                        headers['Authorization'] = "Token " + access_token;
                    return [4 /*yield*/, window['fetch']("" + defaultBasePath + url, {
                            method: method,
                            headers: headers,
                            body: JSON.stringify(body)
                        })];
                case 1:
                    response = _a.sent();
                    if (response.status === 401) {
                        setToken(null);
                        throw new Error('401');
                    }
                    return [4 /*yield*/, response.json()];
                case 2:
                    result = _a.sent();
                    if (!response.ok)
                        throw result;
                    return [2 /*return*/, result];
            }
        });
    });
}
export function get(url) {
    return fetchAsync('GET', url);
}
export function post(url, body) {
    return fetchAsync('POST', url, body);
}
export function del(url) {
    return fetchAsync('DELETE', url);
}
export function put(url, body) {
    return fetchAsync('PUT', url, body);
}
export function toQueryString(obj) {
    var parts = [];
    for (var i in obj) {
        if (obj.hasOwnProperty(i)) {
            parts.push(encodeURIComponent(i) + "=" + encodeURIComponent(obj[i]));
        }
    }
    return parts.join("&");
}
export function serializeObject(form) {
    var obj = {};
    if (typeof form == 'object' && form.nodeName == "FORM") {
        for (var i = 0; i < form.elements.length; i++) {
            var field = form.elements[i];
            if (field.id
                && field.type != 'file'
                && field.type != 'reset'
                && field.type != 'submit'
                && field.type != 'button') {
                if (field.type == 'select-multiple') {
                    obj[field.id] = '';
                    var tempvalue = '';
                    for (var j = 0; j < form.elements[i].options.length; j++) {
                        if (field.options[j].selected)
                            tempvalue += field.options[j].value + ';';
                    }
                    if (tempvalue.charAt(tempvalue.length - 1) === ';')
                        obj[field.id] = tempvalue.substring(0, tempvalue.length - 1);
                }
                else if ((field.type != 'checkbox' && field.type != 'radio') || field.checked) {
                    obj[field.id] = field.value;
                }
            }
        }
    }
    return obj;
}
//# sourceMappingURL=fetch.js.map