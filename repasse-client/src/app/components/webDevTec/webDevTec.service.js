(function() {
    'use strict';

    angular
        .module('repasse')
        .service('webDevTec', webDevTec);

    /** @ngInject */
    function webDevTec() {
        var data = [{
            'title': 'Funcionamento',
            'url': 'http://repasse.ufabc.edu.br/transfgov/sobre.html',
            'description': 'Saiba como funciona o sistema, a confiabilidade das informações apresentadas e como você pode colaborar enviando sugestões',
            'logo': ''
        }, {
            'title': 'Para desenvolvedores',
            'url': 'http://repasse.ufabc.edu.br/transfgov/sobre.html',
            'description': 'Se você é desenvolvedor e quer contribuir ou criar sua própria aplicação conheça nossa API REST e como enviar solução de bugs',
            'logo': ''
        }, {
            'title': 'Compare',
            'url': 'http://repasse.ufabc.edu.br/transfgov/comparar.html',
            'description': 'Compare os dados de sua cidade com outras e veja a eficácia das transferências do governo federal. Também veja as métricas de evolução para uma determinada área',
            'logo': ''
        }];

        this.getTec = getTec;

        function getTec() {
            return data;
        }
    }

})();