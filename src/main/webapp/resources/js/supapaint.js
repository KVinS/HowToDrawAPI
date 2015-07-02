/**
 * Created by Semyon on 23.03.2015.
 */


function SuperPaint() {
    var $container = null;
    var $layersSelect = null;
    var layers = [];

    var width = 0;
    var height = 0;
    var mode = "normal";
    var color = "#000000";
    var colorA = {r: 0, g: 0, b: 0, a: 0};

    var type = "round"
    var size = 15;
    
    var currentLayer;
    
    this.init = function (_container, _layersSelect) {
        $container = $(_container);
        $layersSelect = $(_layersSelect);
        width = $container.width();
        height = $container.height();

        this.addLayer("Слой -2");
        this.addLayer("Слой -1");
        this.addLayer("Слой 0");
        this.addLayer("Слой 1");
        this.addLayer("Слой 2");
        selectLayerByNum(2);

        var __events = ['mousedown', 'mousemove', 'mouseup', 'mouseleave'];
        for (var i = 0; i < __events.length; i++) {
            var event = __events[i];
            $container.on(event, function (eventName) {
                return function (e) {
                    for (var j = 0; j < layers.length; j++) {
                        var layer = layers[j];
                        if (layer.isEnabled()) {
                            layer[eventName](e);
                        }
                    }
                };
            }(event));
        }
        $layersSelect.material_select();
        $layersSelect.on('change', function () {
            selectLayer(this.value);
        });
    };
    
    this.toggleMod = function (mod) {
        mode = (mode != mod ? mod : "normal");
    };
    
    this.getMod = function() {
        return mode;
    }

    this.setColor = function (hex, rgba) {
        if(currentLayer!=undefined && currentLayer!=null) {
            currentLayer.clearQueue();
        }
        color = "#" + hex;
        colorA = rgba;
    };


    this.setSize = function (newSize) {
        if(currentLayer!=undefined && currentLayer!=null) {
            currentLayer.clearQueue();
        }
        size = newSize;
    }

    this.addLayer = function (name) {
        var layer = createLayer(name).initLayer();
        
        var $option = $("<option value='" + name + "'>" + name + "</option>");

        $layersSelect.append($option);
        selectLayer(name);
        $layersSelect.material_select_update();
    };

    this.cleareLayer = function () {
        currentLayer.clear();
    };

    this.removeLayer = function (name) {
        for (var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            if (layer.name() == name) {
                layer.remove();
                var index = layers.indexOf(layer);
                if (index > -1) {
                    layers.splice(index, 1);
                }
                break;
            }
        }
        var o = $layersSelect.find("option[value='" + name + "']");
        o.remove();
        $layersSelect.material_select_update();
        selectLayerByNum(0);
    };

    function selectLayer(name) {
        for (var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            if (layer.name() == name) {
                layer.setEnabled(true);
                if(currentLayer!=undefined && currentLayer!=null) {
                    currentLayer.clearQueue();
                }
                currentLayer = layer;
            } else {
                layer.setEnabled(false);
            }
        }
        $layersSelect.val(name);
    }

    function selectLayerByNum(num) {
        for (var i = 0; i < layers.length; i++) {
            var layer = layers[i];
            layer.setEnabled(false);
        }
        layers[num].setEnabled(true);
        if(currentLayer!=undefined && currentLayer!=null) {
            currentLayer.clearQueue();
        }
        currentLayer = layers[num];
        $layersSelect.val(layers[num].name());
    }

    function createLayer(name) {
        var layerNum = layers.length;
        var layer = new Layer(layerNum, name, width, height);
        layers.push(layer);
        $container.append(layer.getCanvas());
        return layer;
    }

    function Layer(id, _name, _width, _height) {

        var _this = this;

        var name = _name;

        var width = _width;
        var height = _height;
        var enabled = false;

        var canvas = null;
        var context = null;

        this.offsetLeft = 0;
        this.offsetTop = 0;

        var $layer = $("<canvas id='layer_" + id + "' width='" + width + "' height='" + height + "'>");
        canvas = $layer[0];

        if (typeof G_vmlCanvasManager != 'undefined') {
            canvas = G_vmlCanvasManager.initElement(canvas);
        }
        context = canvas.getContext("2d");

        this.clear = function () {
            this.clearQueue();
        context.clearRect(0,0,width,height);
        };


        this.clearQueue = function () {
            context.closePath();
            clickX.splice(0, clickX.length);
            clickY.splice(0, clickY.length);
            clickColor.splice(0, clickColor.length);
            clickDrag.splice(0, clickDrag.length);
        }

        this.name = function () {
            return name;
        };

        this.initLayer = function () {
            var offset = $layer.offset();
            this.offsetLeft = offset.left;
            this.offsetTop = offset.top;
            return _this;
        };

        this.isEnabled = function () {
            return enabled;
        };

        this.remove = function () {
            $layer.remove();
        };

        this.setEnabled = function (_enabled) {
            enabled = _enabled;
        };

        this.getCanvas = function () {
            return $layer;
        };

        this.getContext = function () {
            return context;
        };


        //drawing specific

        var clickX = [];
        var clickY = [];
        var clickColor = [];
        var clickDrag = [];
        var paint;

        this.mousedown = function (e) {
            var offset = $layer.offset();
            this.offsetLeft = offset.left;
            this.offsetTop = offset.top;

            if (mode == "normal" || mode == "erase") {
                paint = true;
                addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop);
                redraw();
            } else if (mode == "flood") {
                clickX = [];
                clickY = [];
                clickColor = [];
                clickDrag = [];
               
                var cff = new CanvasFloodFiller();
                cff.floodFill(context, Math.floor(e.pageX - this.offsetLeft), Math.floor(e.pageY - this.offsetTop), colorA);
            }
        };

        this.mousemove = function (e) {
            var offset = $layer.offset();
            this.offsetLeft = offset.left;
            this.offsetTop = offset.top;
            if (paint) {
                addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop, true);
                redraw();
            }
        };

        this.mouseup = function (e) {
            paint = false;
        };

        this.mouseleave = function (e) {
            paint = false;
        };

        function addClick(x, y, dragging) {
            clickX.push(x);
            clickY.push(y);
            clickColor.push(colorA);
            clickDrag.push(dragging);
        }

        function redraw() {
            context.lineJoin = type;
            context.lineWidth = size;

            for (var i = 0; i < clickX.length; i++) {
                context.beginPath();
                if (clickDrag[i] && i) {
                    context.moveTo(clickX[i - 1], clickY[i - 1]);
                } else {
                    context.moveTo(clickX[i] - 1, clickY[i]);
                }
                context.lineTo(clickX[i], clickY[i]);
                context.closePath();
                context.strokeStyle = 'rgba('+clickColor[i].r+', '+clickColor[i].g+', '+clickColor[i].b+', '+(clickColor[i].a/255)+')';
                context.fillStyle = 'rgba('+clickColor[i].r+', '+clickColor[i].g+', '+clickColor[i].b+', '+(clickColor[i].a/255)+')';
                context.stroke();
            }
            if (!clickDrag[clickX.length - 1]) {
                clickX = [];
                clickY = [];
                clickColor = [];
                clickDrag = [];
            } else {
                _splice(clickX);
                _splice(clickY);
                _splice(clickColor);
                _splice(clickDrag);
            }
        }

        return this;
    }

    function _splice(array) {
        array.splice(0, array.length - 2);
    }

    function CanvasFloodFiller()
    {
        // Ширина и высота канвы
        var _cWidth = -1;
        var _cHeight = -1;

        // Заменяемый цвет
        var _rR = 0;
        var _rG = 0;
        var _rB = 0;
        var _rA = 0;

        // Цвет закраски
        var _nR = 0;
        var _nG = 0;
        var _nB = 0;
        var _nA = 0;

        var _data = null;

        /*
         * Получить точку из данных
         **/
        var getDot = function (x, y)
        {
            // Точка: y * ширину_канвы * 4 + (x * 4)
            var dstart = (y * _cWidth * 4) + (x * 4);
            var dr = _data[dstart];
            var dg = _data[dstart + 1];
            var db = _data[dstart + 2];
            var da = _data[dstart + 3];

            return {r: dr, g: dg, b: db, a: da};
        }

        /*
         * Пиксель по координатам x,y - готовый к заливке?
         **/
        var tolerance = 25;
        var isNeededPixel = function (x, y, tol)
        {
            var dstart = Math.floor((y * _cWidth * 4) + (x * 4));
            var dr = _data[dstart];
            var dg = _data[dstart + 1];
            var db = _data[dstart + 2];
            var da = _data[dstart + 3];

            

       
           return (
             Math.abs(_rA - da) <= (255 - tolerance) &&
             Math.abs(_rR - dr) <= tolerance &&
             Math.abs(_rG - dg) <= tolerance &&
             Math.abs(_rB - db) <= tolerance
             );
        }

        /*
         * Найти левый пиксель, по пути закрашивая все попавшиеся
         **/
        var findLeftPixel = function (x, y)
        {
            // Крутим пикселы влево, заодно красим. Возвращаем левую границу.
            // Во избежание дубляжа и ошибок, findLeftPixel НЕ красит текущий
            // пиксел! Это сделает обязательный поиск вправо.
            var lx = x - 1;
            var dCoord = (y * _cWidth * 4) + (lx * 4);


            while (lx >= 0 && isNeededPixel(lx,y))
            {
                _data[dCoord] = _nR;
                _data[dCoord + 1] = _nG;
                _data[dCoord + 2] = _nB;
                _data[dCoord + 3] = _nA;

                lx--;
                dCoord -= 4;
            }

            return lx + 1;
        }

        /*
         * Найти правый пиксель, по пути закрашивая все попавшиеся
         **/
        var findRightPixel = function (x, y)
        {
            var rx = x;
            var dCoord = (y * _cWidth * 4) + (x * 4);

            while (rx < _cWidth && isNeededPixel(rx,y))
            {
                _data[dCoord] = _nR;
                _data[dCoord + 1] = _nG;
                _data[dCoord + 2] = _nB;
                _data[dCoord + 3] = _nA;

                rx++;
                dCoord += 4;
            }

            return rx - 1;
        }

        /*
         * Эффективная (строчная) заливка
         **/
        var effectiveFill = function (cx, cy)
        {
            var lineQueue = new Array();

            var fx1 = findLeftPixel(cx, cy);
            var fx2 = findRightPixel(cx, cy);

            lineQueue.push({x1: fx1, x2: fx2, y: cy});

            while (lineQueue.length > 0)
            {
                var cLine = lineQueue.shift();
                var nx1 = cLine.x1;
                var nx2 = cLine.x1;
                var currx = nx2;

                // Сперва для первого пиксела, если верхний над ним цвет подходит,
                // пускаем поиск левой границы.
                // Можно искать вверх?
                if (cLine.y > 0)
                {
                    // Сверху строка может идти левее текущей?
                    if (isNeededPixel(cLine.x1, cLine.y - 1))
                    {

                        // Ищем в том числе влево
                        nx1 = findLeftPixel(cLine.x1, cLine.y - 1);
                        nx2 = findRightPixel(cLine.x1, cLine.y - 1);
                        lineQueue.push({x1: nx1, x2: nx2, y: cLine.y - 1});
                    }

                    currx = nx2;
                    // Добираем недостающее, ищем только вправо, но пока не
                    // доползли так или иначе далее края текущей строки
                    while (cLine.x2 >= nx2 && currx <= cLine.x2 && currx < (_cWidth - 1))
                    {
                        currx++;
                        if (isNeededPixel(currx, cLine.y - 1))
                        {
                            // Сохраняем найденный отрезок
                            nx1 = currx;
                            nx2 = findRightPixel(currx, cLine.y - 1);
                            lineQueue.push({x1: nx1, x2: nx2, y: cLine.y - 1});
                            // Прыгаем далее найденного

                            currx = nx2;
                        }
                    }
                }

                nx1 = cLine.x1;
                nx2 = cLine.x1;
                // Те же яйца, но можно ли искать вниз?
                if (cLine.y < (_cHeight - 1))
                {
                    // Снизу строка может идти левее текущей?
                    if (isNeededPixel(cLine.x1, cLine.y + 1))
                    {
                        // Ищем в том числе влево
                        nx1 = findLeftPixel(cLine.x1, cLine.y + 1);
                        nx2 = findRightPixel(cLine.x1, cLine.y + 1);
                        lineQueue.push({x1: nx1, x2: nx2, y: cLine.y + 1});
                    }

                    currx = nx2;
                    // Добираем недостающее, ищем только вправо, но пока не
                    // доползли так или иначе далее края текущей строки
                    while (cLine.x2 >= nx2 && currx <= cLine.x2 && currx < (_cWidth - 1))
                    {
                        currx++;
                        if (isNeededPixel(currx, cLine.y + 1))
                        {
                            // Сохраняем найденный отрезок
                            nx1 = currx;
                            nx2 = findRightPixel(currx, cLine.y + 1);
                            lineQueue.push({x1: nx1, x2: nx2, y: cLine.y + 1});
                            // Прыгаем далее найденного

                            currx = nx2;

                        }
                    }
                }

            }   // while (main loop)
        }

        /*
         * void floodFill(CanvasContext2D canvasContext, int x, int y)
         * Выполняет заливку на канве
         * canvasContext - контекст
         * int x, y - координаты точки заливки
         * color - цвет заливки
         */
        this.floodFill = function (canvasContext, x, y, color)
        {
            _cWidth = canvasContext.canvas.width;
            _cHeight = canvasContext.canvas.height;

            _nR = color.r;
            _nG = color.g;
            _nB = color.b;
            _nA = color.a;

            var idata = canvasContext.getImageData(0, 0, _cWidth, _cHeight);
            var pixels = idata.data;
            _data = pixels;

            var toReplace = getDot(x, y);
            _rR = toReplace.r;
            _rG = toReplace.g;
            _rB = toReplace.b;
            _rA = toReplace.a;

            // Всё зависнет к известной матери если цвета совпадают
            
            if ((
             Math.abs(_rA - _nA) <= (255 - tolerance) &&
             Math.abs(_rR - _nR) <= tolerance &&
             Math.abs(_rG - _nG) <= tolerance &&
             Math.abs(_rB - _nB) <= tolerance
             ))
                return;

            effectiveFill(x, y);

            canvasContext.putImageData(idata, 0, 0);
        }
    }

}

function hexToRgb(hex) {
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    
    
    return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
    } : null;
}