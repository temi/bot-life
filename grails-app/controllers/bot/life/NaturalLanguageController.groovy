package bot.life

class NaturalLanguageController {
    NaturalLanguageService naturalLanguageService

    def index() {
        naturalLanguageService.analyze();
        render text: [], contentType: 'application/json'
    }
}
